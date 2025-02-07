package io.github.reinershir.boot.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.reinershir.auth.core.model.Role;
import io.github.reinershir.auth.core.support.AuthorizeManager;
import io.github.reinershir.auth.entity.TokenInfo;
import io.github.reinershir.boot.common.Result;
import io.github.reinershir.boot.contract.ShirBootContracts;
import io.github.reinershir.boot.core.international.IMessager;
import io.github.reinershir.boot.dto.req.UserListDTO;
import io.github.reinershir.boot.dto.req.UserReqDTO;
import io.github.reinershir.boot.dto.res.UserListRespDTO;
import io.github.reinershir.boot.exception.BusinessException;
import io.github.reinershir.boot.exception.DataIncorrectException;
import io.github.reinershir.boot.mapper.UserMapper;
import io.github.reinershir.boot.model.User;
import io.github.reinershir.boot.service.IUserService;
import io.github.reinershir.boot.utils.MD5;
import jakarta.servlet.http.HttpServletRequest;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService{

	@Autowired(required = false)
	AuthorizeManager authorizeManager;
	
	Logger logger=LoggerFactory.getLogger(this.getClass());
	
	@Value(value="${lui-auth.authrizationConfig.administratorId}")
	String administratorId;
	
	/**
	 * @Title: login
	 * @Description:   验证用户名密码是否正确
	 * @author ReinerShir
	 * @param loginName
	 * @param password
	 * @return 返回user表示登陆成功，返回null表示登陆失败
	 * @throws Exception 
	 */
	public User login(String loginName,String password) throws Exception{
		User entity = new User();
		entity.setLoginName(loginName);
		//entity.setPassword(password);
		QueryWrapper<User> queryWrapper = new QueryWrapper<>(entity);
		User user = baseMapper.selectOne(queryWrapper);
		if(user==null) {
			throw new BusinessException("登陆户名不存在！");
		}
		if(user.getStatus()==ShirBootContracts.STATUS_DISABLE) {
			throw new BusinessException("该账号已被禁用！");
		}
		
		String requiredPwd = getEncodingString(loginName,password);
		if(user.getPassword().equals(requiredPwd)) {
			User storeUser = new User();
			storeUser.setId(user.getId());
			storeUser.setLoginName(user.getLoginName());
			storeUser.setCreateDate(user.getCreateDate());
			//storeUserInfo.put("userGroupType", user.getUserGroupType());
			String token = authorizeManager.generateToken(user.getId().toString(), 1);
			storeUser.setPassword(token);
			return storeUser;
		}
		return null;
	}
	
	/**
	 * @Title: getEncodingString
	 * @Description:   对密码进行MD5 + 盐
	 * @author ReinerShir
	 * @date 2020年12月4日
	 * @param salt 盐，使用loginName
	 * @param password 密码
	 * @return MD5后的字符串
	 */
	private String getEncodingString(String salt,String password) {
		String encodingPwd = MD5.encode(password + ShirBootContracts.LOGIN_SALT);
		//取后8位作为盐增加彩虹表破解难度
		String requiredPwd = MD5.encode(salt).substring(8)+encodingPwd;
		return requiredPwd;
	}
	
	@Transactional
	public User insert(UserReqDTO user) {
		user.setIsDelete(0);
		user.setCreateDate(new Date());
		User entity = new User();
		entity.setLoginName(user.getLoginName());
		entity.setIsDelete(0);
		if(baseMapper.selectCount(new QueryWrapper<>(entity))>0) {
			throw new BusinessException(IMessager.getInstance().getMessage("message.user.exsist"));
		}
		user.setPassword(getEncodingString(user.getLoginName(),user.getPassword()));
		int result = baseMapper.insert(user);
		if(result>0) {
			//为用户绑定角色
			if(!CollectionUtils.isEmpty(user.getRoleIds())) {
				authorizeManager.getRoleAccess().bindRoleForUser(user.getId()+"", user.getRoleIds());
			}
		}
		return user;
	}
	
	@Transactional
	public int updateUser(UserReqDTO user) {
		user.setUpdateDate(new Date());
		//不能修改的字段,偷懒没有新建一个DTO
		user.setLoginName(null);
		user.setPassword(null);
		user.setIsDelete(null);
		user.setCreateDate(null);
		int result =  baseMapper.updateById(user);
		if(result>0) {
			//为用户绑定角色
			if(!CollectionUtils.isEmpty(user.getRoleIds())) {
				authorizeManager.getRoleAccess().bindRoleForUser(user.getId()+"", user.getRoleIds());
			}
		}
		return result;
	}
	
	public int logicDeleteUser(Long id) {
		if(!id.toString().equals(administratorId)) {
			return baseMapper.deleteById(id);
		}
		return -1;
	}
	
	public Page<UserListRespDTO> list(UserListDTO dto){
		User entity = new User();
		BeanUtils.copyProperties(dto, entity);
		
		QueryWrapper<User> queryWrapper = new QueryWrapper<>(entity);
		if(dto.getRoleId()!=null) {
			List<String> userIds = authorizeManager.getRoleAccess().selectUserIdByRole(dto.getRoleId());
			if(!CollectionUtils.isEmpty(userIds)) {
				queryWrapper.in("ID",userIds);
			}else {
				logger.warn("根据角色ID查询了空记录的用户列表，角色ID:{}",dto.getRoleId());
				return new Page<UserListRespDTO>(dto.getPage(),dto.getPageSize(),0l);
			}
		}
		if(entity.getNickName()!=null) {
			queryWrapper.like("NICK_NAME", entity.getNickName());
			entity.setNickName(null);
		}
		if(entity.getPhoneNumber()!=null) {
			queryWrapper.like("PHONE_NUMBER", entity.getPhoneNumber());
			entity.setPhoneNumber(null);
		}
		queryWrapper.ge(dto.getStartDate()!=null, "CREATE_DATE", dto.getStartDate());
		queryWrapper.le(dto.getEndDate()!=null, "CREATE_DATE", dto.getEndDate());
		queryWrapper.orderByDesc("CREATE_DATE");
		Page<User> page = baseMapper.selectPage(new Page<>(dto.getPage(),dto.getPageSize()),queryWrapper);
		List<UserListRespDTO> list = new ArrayList<>();
		page.getRecords().forEach((record)->{
			record.setPassword(null);
			UserListRespDTO resp = new UserListRespDTO();
			resp.setCreateDate(record.getCreateDate());
			resp.setLoginName(record.getLoginName());
			resp.setId(record.getId());
			resp.setNickName(record.getNickName());
			resp.setStatus(record.getStatus());
			resp.setPhoneNumber(record.getPhoneNumber());
			resp.setUpdateDate(record.getUpdateDate());
			List<Long> roleIds = authorizeManager.getRoleAccess().getRoleIdByUser(resp.getId().toString());
			List<Role> roleList = authorizeManager.getRoleAccess().selectByList(roleIds);
			StringBuilder roleName = new StringBuilder();
			if(!CollectionUtils.isEmpty(roleList)) {
				for (Iterator<Role> i = roleList.iterator(); i.hasNext();) {
					roleName.append(i.next().getRoleName());
					if(i.hasNext()) {
						roleName.append(",");
					}
				}
			}
			resp.setRoleIds(roleIds);
			resp.setRoleName(roleName.toString());
			list.add(resp);
		});
		Page<UserListRespDTO> pageBean =  new Page<UserListRespDTO>(page.getCurrent(),page.getSize(),page.getTotal());
		pageBean.setRecords(list);
		return pageBean;
	}

	public User getById(Long id) {
		return baseMapper.selectById(id);
	}
	
	
	/**
	 * @Title: getRoleByUser
	 * @Description:   获取该用户绑定的角色ID
	 * @author ReinerShir
	 * @param userId
	 * @return  返回角色ID列表
	 */
	public List<Long> getRoleByUser(String userId){
		return authorizeManager.getRoleAccess().getRoleIdByUser(userId);
	}
	
	public Result<Object> updatePassword(HttpServletRequest request,String password,String newPassword) {
		TokenInfo tokenInfo = authorizeManager.getTokenInfo(request);
		if(tokenInfo==null) {
			throw new DataIncorrectException("非法的token！");
		}
		Long userId = Long.parseLong(tokenInfo.getUserId());
		User user = getById(userId);
		if(user!=null) {
			String requiredPassword = getEncodingString(user.getLoginName(),password);
			if(user.getPassword().equals(requiredPassword)) {
				user.setPassword(getEncodingString(user.getLoginName(),newPassword));
				user.setUpdateDate(new Date());
				if(baseMapper.updateById(user)>0) {
					return Result.ok();
				}
			}else {
				return Result.failed(IMessager.getInstance().getMessage("message.user.unmatchPassword"));
			}
		} 
		return Result.failed();
	}
	
	/**
	 * @Title: resetPassword
	 * @Description:   重置密码为123456
	 * @author ReinerShir
	 * @date 2020年12月4日
	 * @param userId
	 * @return
	 */
	public boolean resetPassword(Long userId,String password) {
		User user = getById(userId);
		if(user!=null) {
			user.setPassword(this.getEncodingString(user.getLoginName(),password));
			if(baseMapper.updateById(user)>0) {
				return true;
			}
		}
		return false;
	}
	
	public void logout(HttpServletRequest request) {
		authorizeManager.logout(request);
	}
	
	public boolean disbleOrEnable(Long id,Integer status) {
		User user = baseMapper.selectById(id);
		if(user!=null) {
			user.setStatus(status);
			return baseMapper.updateById(user)>0?true:false;
		}
		return false;
	}
	
}
