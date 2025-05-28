package com.halcyon.aiagentojbackend.model.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.halcyon.aiagentojbackend.model.entity.Question;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目封装类
 */
@Data
public class QuestionVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题目名称
     */
    private String title;

    /**
     * 题目标签
     */
    private List<String> tags;

    /**
     * 题目描述
     */
    private String content;


    /**
     * 题目答案
     */
    private String answer;

    /**
     * 提交次数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建题目人的信息
     */
    private UserVO userVO;

    /**
     * 封装类转对象
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionVO questionVO){
        if(questionVO == null){
            return null;
        }
        Question question = new Question();
        BeanUtil.copyProperties(questionVO,question);
        List<String> tags = questionVO.getTags();
        if(tags != null){
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        return question;
    }

    /**
     * 对象转封装类
     * @param question
     * @return
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtil.copyProperties(question, questionVO);
        List<String> tagList = JSONUtil.toList(question.getTags(), String.class);
        questionVO.setTags(tagList);
        return questionVO;
    }

    private static final long serialVersionUID = 1L;
}
