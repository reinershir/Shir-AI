
from llama_index.core import VectorStoreIndex, SimpleDirectoryReader
from llama_index.tools.google.search.base import GoogleSearchToolSpec
from llama_index.agent.openai import OpenAIAgent
from llama_index.llms.openai import OpenAI

from llama_index.core.memory import ChatMemoryBuffer
from llama_index.core.chat_engine import CondensePlusContextChatEngine

import uuid
import os
# import openai
import json
from flask_cors import CORS,cross_origin
import logging
import sys
from flask import Flask, Response, request
logging.basicConfig(
                    level    = logging.INFO,              # 定义输出到文件的log级别，                                                            
                    format   = '%(asctime)s  %(filename)s : %(levelname)s  %(message)s',    # 定义输出log的格式
                    datefmt  = '%Y-%m-%d %A %H:%M:%S',                                     # 时间
#                    filename = 'gpt-index.log',                # log文件名
#                    filemode = 'w',
                    stream=sys.stdout)

# 设置api代理地址
# os.environ["OPENAI_API_BASE"] = 'https://xxx.com/v1'

app = Flask(__name__)
# 解决跨域问题
@app.after_request
def after_request(response):
    response.headers.add('Access-Control-Allow-Origin', '*')
    response.headers.add('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,OPTIONS')
    response.headers.add('Access-Control-Allow-Headers', 'Content-Type,Authorization')
    response.headers.add('Access-Control-Allow-Credentials', 'true')
    return response



memory = ChatMemoryBuffer.from_defaults(token_limit=3900)
@cross_origin
@app.route('/chatStream',methods=['POST'])
def chatStream():
    # user_id = request.args.get('userId')  
    model = request.form.get('model')  
    question = request.form.get('question') 
    session_id = request.form.get('sessionId')
    method = request.form.get('method')
    lang = request.form.get('lang')
    print(model)

    print("用户： 提交了问题："+question+" model："+model+" session_id："+session_id + "语言： "+lang)

    if lang == "zh_CN":
        question += " 使用中文返回"

    def eventStream():
        event_name="message"
        answer = ""
        try: 
            api_key = get_random_api_key()
            logging.info("当前的apikey是："+api_key)
            os.environ["OPENAI_API_KEY"] = api_key
            
            response_stream = None
            if 'google'.__eq__(method):
                gsearch_tools = GoogleSearchToolSpec(key="AIzaSyDRw4plAsqNUyLQ6DtL1wzB_7vUiBbFehg", engine="10ff9de008ee44703").to_tool_list()
                # Create the Agent with our tools
                agent = OpenAIAgent.from_tools(gsearch_tools, verbose=True,streaming=True,model=model)
                response_stream = agent.stream_chat(question)
                # response_stream.print_response_stream()
            else:
                dir_path = "./file/uploads/"+session_id
                if not os.path.exists(dir_path):
                    os.makedirs(dir_path)
                documents = SimpleDirectoryReader(dir_path).load_data()
                index = VectorStoreIndex.from_documents(documents)
                chat_engine = CondensePlusContextChatEngine.from_defaults(
                    index.as_retriever(),
                    memory=memory,
                    streaming=True,
                    llm=OpenAI(model=model),
                    context_prompt=(
                        "You are a chatbot, able to have normal interactions, as well as talk"
                        "Here are the relevant documents for the context:\n"
                        "{context_str}"
                        "\nInstruction: Use the previous chat history, or the context above, to interact and help the user "
                        "Remember, use obvious line break symbols like: \n when the returned content needs to be wrapped."
                    ),
                    verbose=True,
                )
                response_stream = chat_engine.stream_chat(question)
                # query_engine = index.as_query_engine(streaming=True,similarity_top_k=2,model=model)
                #response_stream = query_engine.query(question)
                
            id=uuid.uuid1()
            if  hasattr(response_stream,'response_gen'):
                for text in response_stream.response_gen:
                    answer+= text
                    msg = str(text)
                    print(msg, end="")
                    # response_json =  json.dumps({"stat": "SUCCESS", "content": str(text)})
                    msg = msg.replace('\n', '<br>')
                    yield f'id: {id}\nevent: {event_name}\ndata: {msg}\n\n'
            # answer = openai.ChatCompletion.create(model="gpt-3.5-turbo", messages=[{"role": "user", "content": question}])["choices"][0]["message"]["content"]
            logging.info('ai处理完成，回答内容：'+answer)
            str_done = f'id: {id}\nevent: [DONE]\ndata: [DONE]\n\n'
            yield str_done
            
        except ValueError as e:
            print(e)
            response_json = {"stat": "FAILED", "content": str(e)}
            id=uuid.uuid1()
            str_out = f'id: {id}\nevent: {event_name}\ndata: {json.dumps(response_json)}\n\n'
            yield str_out
            #end token
            # str_out = f'id: {id}\nevent: {event_name}\ndata: {json.dumps({"stat": "SUCCESS", "message": "[DONE]"})}\n\n'
            # yield str_out

    return Response(eventStream(), mimetype="text/event-stream")


api_keys = []
def get_random_api_key():
    # global api_keys
    # if len(api_keys) == 0 :
    #     from ApiKeyLoader import ApiKeyLoader
    #     loader = ApiKeyLoader()
    #     apiKey = loader.load_api_keys_from_database()
    #     if "," in apiKey:
    #         api_keys = apiKey.split(",")
    #     elif len(apiKey) > 0:
    #         api_keys = [apiKey]
    #     else:
    #         api_keys = ["sk-xxxx"]
    # # 随机选择一个API Key
    # return random.choice(api_keys)
    return "sk-xxxx"

if __name__ == '__main__':
    app.run(debug=True,host="0.0.0.0", port=9960)