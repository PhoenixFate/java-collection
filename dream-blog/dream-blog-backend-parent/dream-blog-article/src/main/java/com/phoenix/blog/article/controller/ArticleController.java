package com.phoenix.blog.article.controller;

import com.phoenix.blog.article.request.ArticleRequest;
import com.phoenix.blog.article.request.ArticleUserRequest;
import com.phoenix.blog.article.service.IArticleService;
import com.phoenix.blog.article.util.AuthUtil;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.common.constant.ArticleStatusEnum;
import com.phoenix.blog.common.request.UserInfoRequest;
import com.phoenix.blog.entity.Article;
import com.phoenix.blog.entity.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 文章信息表 前端控制器
 * </p>
 *
 * @author phoenix
 * @since 2022-10-12
 */
@RestController
@RequestMapping("/article")
@AllArgsConstructor
@Api(tags = "文章")
public class ArticleController {

    private final IArticleService articleService;

    /**
     * 带条件查询的文章分页列表
     *
     * @param articleRequest 文章条件查询
     * @return 文章分页列表
     */
    @PostMapping("/list")
    @ApiOperation("带条件查询的文章分页列表")
    @ApiImplicitParam(name = "articleRequest", value = "带分页的文章查询对象", dataType = "ArticleRequest", required = true)
    public Result page(@RequestBody ArticleRequest articleRequest) {
        return articleService.queryPage(articleRequest);
    }

    /**
     * 查询文章详情接口
     *
     * @param id 文章id
     * @return 文章详情
     */
    @GetMapping("/{id}")
    @ApiOperation("查询文章详情接口")
    @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "String")
    public Result detail(@PathVariable("id") String id) {
        //通过SpringSecurity提供的SecurityContextHolder的来获取当前的认证用户
        //获取认证信息对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        //decodedDetails中有详细的用户信息
        Map<String, Object> decodedDetails = (Map<String, Object>) details.getDecodedDetails();
        //登录的时候，根据用户名查询的用户详情数据
        Map<String, Object> userInfo = (Map<String, Object>) decodedDetails.get("userInfo");
        //也可以通过封装util上获取userInfo信息
        SysUser sysUser = AuthUtil.getUserInfo();

        return articleService.findArticleAndLabelListById(id);
    }

    /**
     * 修改文章信息接口
     *
     * @param article 文章
     * @return 是否修改成功
     */
    @ApiOperation("修改文章信息接口")
    @PutMapping("/")
    @ApiImplicitParam(name = "article", value = "文章对象", dataType = "Article", required = true)
    public Result update(@RequestBody Article article) {
        return articleService.updateOrSave(article);
    }

    /**
     * 新增文章信息接口
     *
     * @param article 文章
     * @return 是否修改成功
     */
    @ApiOperation("新增文章信息接口")
    @PostMapping("/")
    @ApiImplicitParam(name = "article", value = "文章对象", dataType = "Article", required = true)
    public Result save(@RequestBody Article article) {
        return articleService.updateOrSave(article);
    }

    /**
     * 根据文章id删除文章（软删除，更新状态）
     *
     * @param id 文章id
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    @ApiOperation("根据文章id删除文章")
    @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "String")
    public Result delete(@PathVariable("id") String id) {
        return articleService.updateStatus(id, ArticleStatusEnum.DELETE);
    }

    /**
     * 审核通过文章
     *
     * @param id 文章id
     * @return 成功
     */
    @PutMapping("/audit/success/{id}")
    @ApiOperation("审核通过文章")
    @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "String")
    public Result approve(@PathVariable("id") String id) {
        return articleService.updateStatus(id, ArticleStatusEnum.SUCCESS);
    }

    /**
     * 审核不通过文章
     *
     * @param id 文章id
     * @return 成功
     */
    @PutMapping("/audit/failure/{id}")
    @ApiOperation("审核不通过文章")
    @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "String")
    public Result failure(@PathVariable("id") String id) {
        return articleService.updateStatus(id, ArticleStatusEnum.FAILURE);
    }

    /**
     * 根据用户id查询公开或者未公开的文章列表接口
     *
     * @param articleUserRequest 用户id和是否公开
     * @return 某用户的文章列表
     */
    @PostMapping("/user")
    @ApiOperation("根据用户id查询公开或者未公开的文章列表接口")
    @ApiImplicitParam(name = "articleUserRequest", value = "用户id和是否公开", dataType = "ArticleUserRequest", required = true)
    public Result findByUserId(@RequestBody ArticleUserRequest articleUserRequest) {
        return articleService.findListByUserId(articleUserRequest);
    }

    @ApiOperation("更新文章点赞数")
    @PutMapping("/likes/{id}/{count}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "count", value = "点赞数", required = true, dataType = "int")
    })
    public Result updateLikesNumber(@PathVariable("id") String id, @PathVariable("count") Integer count) {
        return articleService.updateLikesNumber(id, count);
    }

    /**
     * 更新文章浏览次数
     *
     * @param id 文章id
     * @return 是否更新成功
     */
    @ApiOperation("更新文章浏览次数")
    @PutMapping("/view/count/{id}")
    @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "String")
    public Result updateViewCount(@PathVariable("id") String id) {
        return articleService.updateViewCount(id);
    }

    /**
     * 统计文章总记录数（审核通过且公开的）
     *
     * @return 文章总记录数
     */
    @ApiOperation("审核通过并且公开的文章总记录数")
    @GetMapping("/total")
    public Result getArticleTotal() {
        return articleService.getArticleTotal();
    }

    @ApiOperation("统计各分类下的文章数")
    @GetMapping("/category/total")
    public Result categoryTotal() {
        return articleService.selectCategoryTotal();
    }

    @ApiOperation("统计近6个月发布的文章数量")
    @GetMapping("/month/total")
    public Result monthArticleTotal() {
        return articleService.selectMonthArticleTotal();
    }

    @ApiOperation("Feign接口-更新文章表和评论表中的用户信息")
    @PutMapping("/user")
    @ApiImplicitParam(name = "userInfoRequest", value = "用户信息对象", dataType = "UserInfoRequest", required = true)
    public boolean updateUserInfo(@RequestBody UserInfoRequest userInfoRequest) {
        return articleService.updateUserInfo(userInfoRequest);
    }

}
