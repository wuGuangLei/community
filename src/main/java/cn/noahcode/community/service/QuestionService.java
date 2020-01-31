package cn.noahcode.community.service;

import cn.noahcode.community.dto.PaginationDOT;
import cn.noahcode.community.dto.QuestionDTO;
import cn.noahcode.community.mapper.QuestionMapper;
import cn.noahcode.community.mapper.UserMapper;
import cn.noahcode.community.model.Question;
import cn.noahcode.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author NoahCode
 * @date 2019/12/4
 * @description
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDOT list(Integer page, Integer size) {
        PaginationDOT paginationDOT = new PaginationDOT();
        Integer totalCount = questionMapper.count();
        paginationDOT.setPagination(totalCount, page, size);
        if (page > paginationDOT.getTotalPage()) {
            page = paginationDOT.getTotalPage();
        }
        if (page < 1) {
            page = 1;
        }
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDOT.setQuestions(questionDTOList);
        return paginationDOT;
    }

    public PaginationDOT list(String id, Integer page, Integer size) {
        PaginationDOT paginationDOT = new PaginationDOT();
        Integer totalCount = questionMapper.countById(id);
        paginationDOT.setPagination(totalCount, page, size);
        if (page < 1) {
            page = 1;
        }
        if (page > paginationDOT.getTotalPage()) {
            page = paginationDOT.getTotalPage();
        }
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.listById(id, offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDOT.setQuestions(questionDTOList);
        return paginationDOT;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        System.out.println(question.getId());
        if (question.getId() == null) {
            //插入
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        } else {
            //更新
            question.setGmtModified(question.getGmtCreate());
            questionMapper.update(question);
        }
    }
}
