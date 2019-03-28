package cn.itxsl.kernel.repository;

import cn.itxsl.kernel.model.mapped.ITMessage;
import cn.itxsl.kernel.repository.base.Repository;
import org.springframework.stereotype.Component;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/21 19:36
 * @description：留言管理
 */
@Component
public class MessageRepository extends Repository<ITMessage> {
}
