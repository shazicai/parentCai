package cn.com.springCai.mapper.secondaryMapper;


import cn.com.springCai.service.entity.testUser.po.TestUser;
import cn.com.springCai.service.entity.testUser.vo.TestUserVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LK
 * @version V1.0
 * @date 2020
 */
@Repository
public interface TestUserMapperSecondary {

	/**
	 * 保存信息
	 * @param po
	 * @return
	 */
	int save(TestUser po);

	/**
	 * 查询
	 * @param po
	 * @return
	 */
	List<TestUserVo> list(TestUser po);

	/**
	 * 数据条数
	 * @param po
	 * @return
	 */
	int count(TestUser po);
}