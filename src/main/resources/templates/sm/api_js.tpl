import request from '@/utils/request'

export function fetchList(data) {
  return request({
    url: '/${ClassName?uncap_first}',
    method: 'get',
    params: data
  })
}

export function create(data) {
  return request({
    url: '/${ClassName?uncap_first}',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data
  })
}

export function update(data) {
  return request({
    url: '/${ClassName?uncap_first}',
    method: 'put',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data
  })
}

export function deleteById(data) {
  return request({
    url: '/${ClassName?uncap_first}/' + data,
    method: 'delete'
  })
}
