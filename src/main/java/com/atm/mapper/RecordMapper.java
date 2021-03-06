package com.atm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.atm.bean.Record;

@MapperScan
public interface RecordMapper {
	
	public List<Record> getTranfer(@Param("operId")Integer operId,
			@Param("aimId")Integer aimId,@Param("operType")Integer operType,
			@Param("time")Integer time);

	public int insertTrans(@Param("operId")Integer operId,
			@Param("aimId")Integer aimId,@Param("operType")Integer operType,@Param("money")Integer money);
	
}
