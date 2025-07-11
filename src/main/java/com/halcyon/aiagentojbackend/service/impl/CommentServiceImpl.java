package com.halcyon.aiagentojbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.halcyon.aiagentojbackend.exception.ErrorCode;
import com.halcyon.aiagentojbackend.exception.ThrowUtils;
import com.halcyon.aiagentojbackend.model.dto.comment.CommentAddRequest;
import com.halcyon.aiagentojbackend.model.dto.comment.CommentQueryRequest;
import com.halcyon.aiagentojbackend.model.dto.question.QuestionQueryRequest;
import com.halcyon.aiagentojbackend.model.entity.Question;
import com.halcyon.aiagentojbackend.model.entity.User;
import com.halcyon.aiagentojbackend.model.vo.CommentVO;
import com.halcyon.aiagentojbackend.model.vo.UserVO;
import com.halcyon.aiagentojbackend.service.UserService;
import com.halcyon.aiagentojbackend.model.entity.Comment;
import com.halcyon.aiagentojbackend.service.CommentService;
import com.halcyon.aiagentojbackend.mapper.CommentMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author 张嘉鑫
* @description 针对表【comment(评论表)】的数据库操作Service实现
* @createDate 2025-07-04 09:48:55
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Resource
    private UserService userService;

    @Override
    public long addComment(CommentAddRequest commentAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(commentAddRequest == null, ErrorCode.PARAMS_ERROR,"参数不能为空");
        String content = commentAddRequest.getContent();
        ThrowUtils.throwIf(content == null || content.isEmpty(),ErrorCode.PARAMS_ERROR,"评论内容不能为空");
        ThrowUtils.throwIf(content.length() > 500,ErrorCode.PARAMS_ERROR,"评论内容不能过长");
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentAddRequest,comment);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR,"未登录");
        comment.setUserId(loginUser.getId());
        //点赞数设置
        comment.setThumbNum(0);
        //todo 用事务
        boolean result = this.save(comment);
        //通过questionId拿到评论map
        Long questionId = comment.getQuestionId();
        Map<Long,Comment> commentMap = this.list(new QueryWrapper<Comment>().eq("questionId",questionId)).stream().collect(Collectors.toMap(Comment::getId,c->c));
        if(comment.getBeCommentId() != null){
            //找到根父评论
            Long rootParentId = findRootParentId(comment.getBeCommentId(), commentMap);
            //更新父评论的回复数量
            Comment rootParent = this.getById(rootParentId);
            if (rootParent != null) {
                rootParent.setReplyNum(rootParent.getReplyNum() + 1);
                this.updateById(rootParent);
            }
        }
        ThrowUtils.throwIf(!result,ErrorCode.OPERATION_ERROR,"添加评论失败");
        return comment.getId();
    }
    /**
     * 查找评论的根父评论id
     * @param commentId
     * @param commentMap
     * @return
     */
    private Long findRootParentId(Long commentId, Map<Long,Comment> commentMap){
        Comment comment = commentMap.get(commentId);
        if(comment.getBeCommentId() == null) return commentId;
        return findRootParentId(comment.getBeCommentId(),commentMap);
    }

    @Override
    public List<CommentVO> listCommentsByQuestionId(Long questionId) {
        //获取该问题下所有的评论
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("questionId",questionId);
        queryWrapper.eq("isDelete",0);
        queryWrapper.orderByDesc("createTime");
        List<Comment> commentList = this.list(queryWrapper);
        if(CollectionUtil.isEmpty(commentList)){
            return new ArrayList<>();
        }
        //获取所有评论用户id
        Set<Long> userIds = commentList.stream().map(Comment::getUserId).collect(Collectors.toSet());
        //获取用户信息
        Map<Long, UserVO> userVOMap = userService.listByIds(userIds).stream()
                .map(user -> userService.getUserVO(user))
                .collect(Collectors.toMap(UserVO::getId,userVO -> userVO));
        //转换为VO
        List<CommentVO> commentVOList = commentList.stream()
                .map(comment -> {
                    CommentVO commentVO = new CommentVO();
                    BeanUtil.copyProperties(comment, commentVO);
                    commentVO.setUserVO(userVOMap.get(comment.getUserId()));
                    return commentVO;
                }).toList();
        //构建评论树
        return buildCommentTree(commentVOList);
    }

    private List<CommentVO> buildCommentTree(List<CommentVO> commentVOList){
        Map<Long, CommentVO> commentVOMap = commentVOList.stream().collect(Collectors.toMap(CommentVO::getId, commentVO -> commentVO));
        //存储所有顶级评论
        List<CommentVO> rootComments = new ArrayList<>();
        for(CommentVO commentVO : commentVOList){
            Long beCommentId = commentVO.getBeCommentId();
            if(beCommentId == null){
                //最顶级父评论
                rootComments.add(commentVO);
            }else {
                //子评论，找到他的父评论，添加进children里
                CommentVO parentCommentVO = commentVOMap.get(beCommentId);
                if(parentCommentVO != null){
                    if(parentCommentVO.getChildren() == null){
                        parentCommentVO.setChildren(new ArrayList<>());
                    }
                    parentCommentVO.getChildren().add(commentVO);
                }
            }
        }
        //对子评论进行排序
        sortCommentChildren(rootComments);
        return rootComments;
    }

    /**
     * 对评论的子评论进行排序
     * @param commentVOList
     */
    private void sortCommentChildren(List<CommentVO> commentVOList){
        if(CollectionUtil.isEmpty(commentVOList)) return;
        for(CommentVO commentVO : commentVOList){
            if(commentVO.getChildren() != null){
                //按照创建时间进行排序
                commentVO.setChildren(commentVO.getChildren().stream().sorted((a,b) -> b.getCreateTime().compareTo(a.getCreateTime())).toList());
                //递归对子评论的评论进行排序
                sortCommentChildren(commentVO.getChildren());
            }
        }
    }
    @Override
    public QueryWrapper<Comment> getQueryWrapper(CommentQueryRequest commentQueryRequest) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        if(commentQueryRequest == null){
            return queryWrapper;
        }
        Long id = commentQueryRequest.getId();
        Long userId = commentQueryRequest.getUserId();
        Long questionId = commentQueryRequest.getQuestionId();
        String content = commentQueryRequest.getContent();
        Long beCommentId = commentQueryRequest.getBeCommentId();
        String sortField = commentQueryRequest.getSortField();
        String sortOrder = commentQueryRequest.getSortOrder();

        //拼接查询条件
        queryWrapper.like(StrUtil.isNotBlank(content),"content",content);
        queryWrapper.eq(ObjUtil.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjUtil.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjUtil.isNotEmpty(beCommentId), "beCommentId", beCommentId);
        queryWrapper.eq(ObjUtil.isNotEmpty(id),"id",id);
        queryWrapper.eq("isDelete",false);
        queryWrapper.orderBy(StrUtil.isNotBlank(sortField),sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public Page<CommentVO> getCommentVOPage(Page<Comment> commentPage) {
        Page<CommentVO> commentVOPage = new Page<>(commentPage.getCurrent(),commentPage.getSize(),commentPage.getTotal());
        List<Comment> commentList = commentPage.getRecords();
        if(commentList == null){
            return commentVOPage;
        }
        //获取所有评论用户的信息
        Set<Long> userIds = commentList.stream()
                .map(Comment::getUserId)
                .collect(Collectors.toSet());
        Map<Long,UserVO> userVOMap = userService.listByIds(userIds).stream()
                .map(user -> userService.getUserVO(user))
                .collect(Collectors.toMap(UserVO::getId,userVO -> userVO));
        //todo 获取用户是否点赞
        //转换为VO并填充用户信息
        List<CommentVO> commentVOList = commentList.stream().map(reply -> {
            CommentVO replyVO = new CommentVO();
            BeanUtil.copyProperties(reply, replyVO);
            replyVO.setUserVO(userVOMap.get(reply.getUserId()));
            return replyVO;
        }).collect(Collectors.toList());
        List<CommentVO> commentVOS = buildCommentTree(commentVOList);
        //将VOS转换成page
        commentVOPage.setRecords(commentVOS);
        commentVOPage.setTotal(commentVOList.size());
        return commentVOPage;
    }

    @Override
    public List<CommentVO> listCommentReplies(long commentId) {
        Comment fatherComment = this.getById(commentId);
        Long questionId = fatherComment.getQuestionId();
        List<Comment> commentList = this.list(new QueryWrapper<Comment>().eq("questionId", questionId));
        if(commentList == null){
            return new ArrayList<>();
        }
        //2.获取所有评论用户的信息
        Set<Long> userIds = commentList.stream().map(Comment::getUserId)
                .collect(Collectors.toSet());
        Map<Long, UserVO> userVOMap = userService.listByIds(userIds).stream()
                .map(user -> userService.getUserVO(user))
                .collect(Collectors.toMap(UserVO::getId, userVO -> userVO));
        //3.转换为VO并填充用户信息
        List<CommentVO> commentVOList = commentList.stream().map(reply -> {
            CommentVO replyVO = new CommentVO();
            BeanUtil.copyProperties(reply, replyVO);
            replyVO.setUserVO(userVOMap.get(reply.getUserId()));
            return replyVO;
        }).toList();
        List<CommentVO> commentVOS = buildCommentTree(commentVOList);
        Map<Long, CommentVO> collect = commentVOS.stream().collect(Collectors.toMap(CommentVO::getId, c -> c));
        return collect.get(commentId).getChildren();
    }

    @Override
    public boolean likeComment(long commentId, long userId) {
        Comment comment = this.getById(commentId);
        ThrowUtils.throwIf(comment == null,ErrorCode.NOT_FOUND_ERROR);
        comment.setThumbNum(comment.getThumbNum()+1);
        //TODO: 防止重复点赞
        return this.updateById(comment);
    }

    @Override
    public boolean removeComment(long commentId, long userId) {
        //获取要删除的评论
        Comment comment = this.getById(commentId);
        ThrowUtils.throwIf(comment == null,ErrorCode.NOT_FOUND_ERROR);
        //校验用户权限，只有本人或者管理员可以删除
        boolean canRemove = comment.getUserId().equals(userId) || userService.isAdmin(userService.getById(userId));
        ThrowUtils.throwIf(!canRemove,ErrorCode.NO_AUTH_ERROR);
        Long questionId = comment.getQuestionId();
        Map<Long,Comment> commentMap = this.list(new QueryWrapper<Comment>().eq("questionId",questionId)).stream().collect(Collectors.toMap(Comment::getId,c->c));
        Long rootParentId = null;
        if(comment.getBeCommentId() != null){
            rootParentId = findRootParentId(comment.getBeCommentId(),commentMap);
        }
        //查找所有需要删除的评论ID
        Set<Long> set = new HashSet<>();
        set.add(commentId);
        findAllChildrenComments(commentId,set,questionId);
        boolean res = this.removeByIds(set);
        ThrowUtils.throwIf(!res,ErrorCode.OPERATION_ERROR);
        //更新回复数
        if(rootParentId != null){
            Comment rootComment = this.getById(rootParentId);
            if(rootComment != null){
                long replyNum = this.count(new QueryWrapper<Comment>().eq("beCommentId", rootParentId));
                rootComment.setReplyNum((int) replyNum);
                this.updateById(rootComment);
            }
        }
        return true;
    }

    /**
     * 递归查找CommentId的子孙id
     * @param commentId
     * @param set
     * @param questionId
     */
    private void findAllChildrenComments(Long commentId,Set<Long> set,Long questionId){
        //查找所有引用该评论的子评论
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("beCommentId",commentId);
        queryWrapper.eq("isDelete",0);
        queryWrapper.eq("questionId",questionId);
        List<Comment> commentList = this.list(queryWrapper);
        for(Comment comment : commentList){
            //将子评论加入待删除集合
            set.add(comment.getId());
            //继续递归查找子评论的子评论
            findAllChildrenComments(comment.getId(),set,questionId);
        }
    }
}




