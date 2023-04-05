package com.tafakkoor.e_learn.controller;

import com.tafakkoor.e_learn.config.security.UserSession;
import com.tafakkoor.e_learn.domain.*;
import com.tafakkoor.e_learn.enums.CommentType;
import com.tafakkoor.e_learn.enums.ContentType;
import com.tafakkoor.e_learn.enums.Levels;
import com.tafakkoor.e_learn.enums.Progress;
import com.tafakkoor.e_learn.services.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.tafakkoor.e_learn.utils.mail.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller

public class UserController {
    private final UserSession userSession;
    private final UserService userService;

    public UserController(UserSession userSession, UserService userService) {
        this.userSession = userSession;
        this.userService = userService;
    }

    @GetMapping({"/practise"})
    public String practise() {
        return "user/practise";
    }

    @GetMapping({"/practise/story"})
    public ModelAndView story() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userLevel", userSession.getLevel());
        modelAndView.setViewName("user/story/levelsStory");
        AuthUser user = userService.getUser(userSession.getId());
        List<Levels> levelsList = userService.getLevels(user.getLevel());
        modelAndView.addObject("levels", levelsList);
        return modelAndView;
    }

    @GetMapping({"/practise/grammar"})
    public ModelAndView grammar() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("levelNotFound", null);
        modelAndView.setViewName("user/grammar/levelsGrammar");
        AuthUser user = userService.getUser(userSession.getId());
        List<Levels> levelsList = userService.getLevels(user.getLevel());
        modelAndView.addObject("levels", levelsList);
        return modelAndView;
    }

    @GetMapping({"/practise/story/{level}"})
    public ModelAndView testStory(@PathVariable String level) {
        ModelAndView modelAndView = new ModelAndView();
        Levels levels;

        try {
            levels = Levels.valueOf(level.toUpperCase());
        } catch (Exception e) {
            modelAndView.addObject("flag", true);
            modelAndView.addObject("levelNotFound", "Level not found named %s".formatted(level.toUpperCase()));
            modelAndView.setViewName("user/levelNotFound");
            return modelAndView;
        }
        AuthUser user = userService.getUser(userSession.getId());
        if (!checkLevel(levels, user.getLevel())) {
            modelAndView.addObject("flag", true);
            modelAndView.addObject("levelNotFound", "I think it's too early for you to try this level %s".formatted(level.toUpperCase()));
            modelAndView.setViewName("user/levelNotFound");
            return modelAndView;
        } else {
            try {
                List<Content> contents = userService.getContentsStories(levels, userSession.getId());
                modelAndView.addObject("flag", Objects.equals(user.getLevel(), levels));
                modelAndView.addObject("stories", contents);
                modelAndView.setViewName("user/story/Stories");
                return modelAndView;
            } catch (Exception var6) {
                return userService.getInProgressPage(modelAndView, var6);
            }
        }
    }

    @GetMapping("/practise/grammar/{level}")
    public ModelAndView testGrammar(@PathVariable String level) {
        ModelAndView modelAndView = new ModelAndView();
        Levels levels;

        try {
            levels = Levels.valueOf(level.toUpperCase());
        } catch (Exception e) {
            modelAndView.addObject("flag", true);
            modelAndView.addObject("levelNotFound", "Level not found named %s".formatted(level.toUpperCase()));
            modelAndView.setViewName("user/levelNotFound");
            return modelAndView;
        }

        AuthUser user = userService.getUser(userSession.getId());
        if (!checkLevel(levels, user.getLevel())) {
            modelAndView.addObject("flag", true);
            modelAndView.addObject("levelNotFound", "I think it's too early for you to try this level %s".formatted(level.toUpperCase()));
            modelAndView.setViewName("user/levelNotFound");
            return modelAndView;
        } else {
            try {
                List<Content> contents = userService.getContentsGrammar(levels, userSession.getId());
                modelAndView.addObject("flag", Objects.equals(user.getLevel(), levels));
                modelAndView.addObject("grammars", contents);
                modelAndView.setViewName("user/grammar/Grammars");
                return modelAndView;
            } catch (Exception var6) {
                return userService.getInProgressPage(modelAndView, var6);
            }
        }
    }

    public boolean checkLevel(@NonNull Levels level, @NonNull Levels userLevel) {
        return level.ordinal() <= userLevel.ordinal();
    }

    @GetMapping({"/practise/stories/{storyId}"})
    public ModelAndView story(@PathVariable String storyId) {
        ModelAndView modelAndView = new ModelAndView();
        AuthUser user = userService.getUser(userSession.getId());
        if (user != null && !user.getLevel().equals(Levels.DEFAULT)) {
            long id;
            try {
                id = Long.parseLong(storyId);
            } catch (Exception var9) {
                modelAndView.addObject("levelNotFound", "Story not found ");
                modelAndView.setViewName("user/levelNotFound");
                return modelAndView;
            }

            UserContent statusContent = userService.checkUserStatus(userSession.getId());
            if (statusContent != null && !Objects.equals(statusContent.getContent().getId(), id)) {
                String var10001 = statusContent.getContent().getContentType().equals(ContentType.GRAMMAR) ? "grammars" : "stories";
                modelAndView.setViewName("redirect:/practise/" + var10001 + "/" + statusContent.getContent().getId());
            } else {
                Content content = userService.getStoryById(id);
                if (content == null) {
                    modelAndView.addObject("levelNotFound", "Story not found ");
                    modelAndView.setViewName("user/levelNotFound");
                } else {
                    List<Comment> comments = userService.getComments(content.getId());
                    List<Vocabulary> vocabularies = userService.getVocabularies(content.getId(), user);
                    modelAndView.addObject("vocabularies", vocabularies);
                    modelAndView.addObject("userId", userSession.getId());
                    modelAndView.addObject("comments", comments);
                    modelAndView.addObject("content", content);
                    modelAndView.setViewName("user/story/readingPage");
                    UserContent userContent = new UserContent(user, content, user.getLevel().equals(content.getLevel()) ? Progress.IN_PROGRESS : Progress.FINISHED);
                    CompletableFuture.runAsync(() -> userService.saveUserContent(userContent));
                }
            }
        } else {
            modelAndView.addObject("levelNotFound", "You have not taken assessment test yet");
            modelAndView.setViewName("user/levelNotFound");
        }
        return modelAndView;
    }

    @GetMapping({"/test"})
    public String test() {
        return "user/story/readingPage";
    }

    @PostMapping({"/practise/story/comments/add/{id}"})
    public ModelAndView addComment(@PathVariable String id, @ModelAttribute("comment") Comment comment) {
        ModelAndView modelAndView = new ModelAndView();
        long contentId;
        try {
            contentId = Long.parseLong(id);
        } catch (Exception var6) {
            modelAndView.addObject("levelNotFound", "Story not found ");
            modelAndView.setViewName("user/levelNotFound");
            return modelAndView;
        }
        Content content = userService.getStoryById(contentId);
        if (content == null) {
            modelAndView.addObject("levelNotFound", "Story not found ");
            modelAndView.setViewName("user/levelNotFound");
        } else {
            comment.setUserId(userService.getUser(userSession.getId()));
            comment.setContentId(content.getId());
            comment.setCommentType(Objects.requireNonNullElse(comment.getCommentType(), CommentType.COMMENT));
            if (comment.getCommentType().equals(CommentType.ANSWER)) {
//                CompletableFuture.runAsync(()->{
                    Optional<Comment> optionalComment = userService.getCommentById(comment.getParentId());
                    Comment parentComment = optionalComment.get();
                    AuthUser user = userService.getUser(parentComment.getUserId().getId());
                    String body = ("Hi %s, %s has replied to your comment on %s content. Here is the comment: \n" +
                            "%s").formatted(Objects.requireNonNullElse(user.getFirstName(), user.getUsername()), userSession.getUser().getUsername(), content.getTitle(), comment.getComment());
                    EmailService.getInstance().sendEmail(user.getEmail(), body, "Answer to your comment");
//                });
            }
            userService.addComment(comment);
            modelAndView.setViewName("redirect:/practise/stories/" + content.getId());
        }
        return modelAndView;
    }

    @PostMapping({"/practise/story/comments/delete/{id}"})
    public ModelAndView deleteComment(@PathVariable String id) {
        ModelAndView modelAndView = new ModelAndView();
        long commentId;
        try {
            commentId = Long.parseLong(id);
        } catch (Exception var6) {
            modelAndView.addObject("levelNotFound", " ");
            modelAndView.setViewName("user/levelNotFound");
            return modelAndView;
        }
        Optional<Comment> commentOptional = userService.getCommentById(commentId);
        if (commentOptional.isEmpty()) {
            modelAndView.addObject("levelNotFound", "Something went wrong");
            modelAndView.setViewName("user/levelNotFound");
        } else {
            Comment comment = commentOptional.get();
            userService.deleteCommentById(comment.getId());
            modelAndView.setViewName("redirect:/practise/stories/" + comment.getContentId());
        }
        return modelAndView;
    }

    @PostMapping({"/practise/story/comments/update/{commentId}"})
    public ModelAndView updateComment(@PathVariable String commentId, @ModelAttribute("comment") Comment comment) {
        ModelAndView modelAndView = new ModelAndView();
        long id;

        try {
            id = Long.parseLong(commentId);
        } catch (Exception var7) {
            modelAndView.addObject("levelNotFound", " ");
            modelAndView.setViewName("user/levelNotFound");
            return modelAndView;
        }

        Optional<Comment> commentOptional = userService.getCommentById(id);
        if (commentOptional.isEmpty()) {
            modelAndView.addObject("levelNotFound", "Something went wrong");
            modelAndView.setViewName("user/levelNotFound");
        } else {
            Comment comment1 = commentOptional.get();
            comment1.setComment(comment.getComment());
            userService.updateComment(comment1);
            modelAndView.setViewName("redirect:/practise/stories/" + comment1.getContentId());
        }
        return modelAndView;
    }

    @PostMapping("/practise/words/add/{id}")
    public ModelAndView addWord(@PathVariable String id, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        long contentId;

        try {
            contentId = Long.parseLong(id);
        } catch (Exception var6) {
            modelAndView.addObject("levelNotFound", "Story not found ");
            modelAndView.setViewName("user/levelNotFound");
            return modelAndView;
        }
        try {
            Optional<Content> content = userService.getContent(contentId);
            List<Vocabulary> vocabularies = userService.mapRequestToVocabularyList(request, content.get(), userSession.getUser());
            userService.addVocabularyList(vocabularies);
        } catch (Exception e) {
//            List<Comment> comments = userService.getComments(content.getId());
//            modelAndView.addObject("userId", userSession.getId());
//            modelAndView.addObject("comments", comments);
//            modelAndView.addObject("content", content);
            e.printStackTrace();
            modelAndView.setViewName("user/story/readingPage");
        }

        Content content = userService.getStoryById(contentId);
        if (content == null) {
            modelAndView.addObject("levelNotFound", "Story not found ");
            modelAndView.setViewName("user/levelNotFound");
        } else {

            modelAndView.setViewName("redirect:/practise/stories/" + content.getId());
        }
        return modelAndView;
    }


    @PostMapping("/practise/words/update/{id}")
    public ModelAndView updateWord(@PathVariable String id, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        long vocabId;
        try {
            vocabId = Long.parseLong(id);
        } catch (Exception e) {
            modelAndView.addObject("levelNotFound", "Vocabulary not found ");
            modelAndView.setViewName("user/levelNotFound");
            return modelAndView;
        }
        Content content = null;
        AuthUser user = userSession.getUser();
        try {
            Optional<Vocabulary> vocabularyOptional = userService.getVocabulary(vocabId);
            Vocabulary vocabulary = vocabularyOptional.get();
            content = vocabulary.getStory();
            userService.mapAndUpdate(request, vocabulary);
            modelAndView.setViewName("redirect:/practise/stories/" + content.getId());
        } catch (RuntimeException e) {
            e.printStackTrace();
            List<Vocabulary> vocabularies = userService.getVocabularies(content.getId(), user);
            List<Comment> comments = userService.getComments(content.getId());
            modelAndView.addObject("userId", userSession.getId());
            modelAndView.addObject("vocabularies", vocabularies);
            modelAndView.addObject("comments", comments);
            modelAndView.addObject("content", content);
            modelAndView.setViewName("user/story/readingPage");
        }
        return modelAndView;
    }

    @PostMapping("/practise/words/delete/{id}")
    public ModelAndView deleteWord(@PathVariable String id, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        long vocabId;
        try {
            vocabId = Long.parseLong(id);
        } catch (Exception e) {
            modelAndView.addObject("levelNotFound", "Story not found ");
            modelAndView.setViewName("user/levelNotFound");
            return modelAndView;
        }
        Content content = null;
        AuthUser user = userSession.getUser();
        try {
            Optional<Vocabulary> vocabularyOptional = userService.getVocabulary(vocabId);
            Vocabulary vocabulary = vocabularyOptional.get();
            content = vocabulary.getStory();
            userService.deleteVocabulary(userSession.getId(), vocabulary);
            modelAndView.setViewName("redirect:/practise/stories/" + content.getId());
        } catch (Exception e) {
            List<Comment> comments = userService.getComments(content.getId());
            List<Vocabulary> vocabularies = userService.getVocabularies(content.getId(), user);
            modelAndView.addObject("userId", userSession.getId());
            modelAndView.addObject("vocabularies", vocabularies);
            modelAndView.addObject("comments", comments);
            modelAndView.addObject("content", content);
            modelAndView.setViewName("user/story/readingPage");
        }
        return modelAndView;
    }

}
