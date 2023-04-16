package com.alicloud.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**********************************
 * @author zhang zhao lin
 * @date 2023年04月16日 12:55
 * @Description 接口转换类
 **********************************/
public class BaseModelVo implements Serializable {

    /**
     *  成功/失败 码值
     */
    private int code;

    /**
     * 成功/失败 说明
     */
    private String message;

    /**
     * 接口返回内容
     */
    private JSONObject result = new JSONObject();

    @JSONField(serialize = false)
    private JSONObject input = new JSONObject();

    @JSONField(serialize = false)
    private List<String> orderBys;

    /**
      ===============分页查看数据===================
     * 当前页
     */
    private Integer pageNo;

    /**
     * 每页数据量
     */
    private Integer pageSize;

    /**
     * 总条数
     */
    private Long total;

    /**
     * 总页数
     */
    private Integer totalPages;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public JSONObject getInput() {
        return input;
    }

    public void setInput(JSONObject input) {
        this.input = input;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<String> getOrderBys() {
        return orderBys;
    }

    public void setOrderBys(List<String> orderBys) {
        this.orderBys = orderBys;
    }

    public enum Code {
        SUCCESS(1,"成功"),
        INIT(0,"初始化"),
        ERROR(-1,"系统异常"),
        LOGINERROR(-2,"登录失败"),
        RESULT_ERROR(-3,"返回结果不符合要求"),
        PARAMETER_ERROR(-400,"入参错误"),
        SUPER_EXCEPTION(-99,"超级异常")
        ,USER_NO_LOGIN(99,"用户未登录")
        ,WAITE_RESULT(10000,"等待结果")
        ,FORBIDDEN(403,"没权限")
        ,NO_FOUND(404,"无法找到接口")
        /**
         * 以100开头的，属于任务执行返回执行的操作
         */
        ,UPLOAD_FILE(101,"需要上传文件")
        ,SAVE_BUT_ERROR(102,"保存成功但核保失败")
        ,ALREAD_RENEWAL(110,"已经投保")

        ,NEED_V_USER(-201,"需要绑定V盟帐号")
        ;
        private int code;

        private String message;

        private Code(int code,String message){
            this.code=code;
            this.message = message;
        }

        public int getCode(){
            return code;
        }
    }


}
