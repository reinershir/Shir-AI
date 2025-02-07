<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${commonPath}.mapper.${ClassName}Mapper">
	<resultMap type="${ClassName}" id="${ClassName}Map">
		<!-- mapping -->
		<#if Autos??>
		<#list Autos as item>
		<#assign info=item?split("#")>
		<result column="${info[8]}" property="${info[0]}"/>		
		</#list>
		</#if>
	</resultMap>
	

	
</mapper>