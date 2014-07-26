<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>

    <head>
        <script src="/Admin/js/jquery-2.0.3.min.js">
        </script>
        <script src="/Admin/js/bootstrap.min.js">
        </script>
        <link href="/Admin/css/bootstrap.min.css" rel="stylesheet" media="screen">
        <link href="/Admin/css/bootstrap-theme.min.css" rel="stylesheet" media="screen">
        <link href="/Admin/css/main.css" rel="stylesheet" media="screen">
        <title>
            信件列表
        </title>
        <script type="text/javascript">
            function validate() {
                var page = $("input[name='searchPageNum']").val();
                if (page < 0) {
                    window.document.location.href = "searchletter.action?searchPageNum=1&key=<s:property value='#session.key'/>";
                } else if (page > <s:property value = "#session.searchTotalpage"/>) {
                    alert("你输入的页数大于最大页数，页面将跳转到首页！");
                    window.document.location.href = "searchletter.action?searchPageNum=1&key=<s:property value='#session.key'/>";
                    return false;
                }

                return true;
            }
        </script>
    </head>

    <body>
        <div class="container">
            <s:if test="#session.searchPageNum==0">
                <p>
                    参数错误！
                </p>
                <p>
                    <a href="index.jsp">
                        返回主页
                    </a>
                </p>
            </s:if>
            <s:else>
                <h1>
                    <font color="blue">
                        分页查询
                    </font>
                </h1>
                <hr>
                <table border="0" align="center" width="50%" class="table table-hover">
                    <tr>
                        <div class="row">
                            <th class="col-sm-1">
                                信件编号
                            </th>
                            <th class="col-sm-2">
                                收件人
                            </th>
                            <th class="col-sm-2">
                                寄件人
                            </th>
                            <th class="col-sm-2">
                                寄件地址
                            </th>
                            <th class="col-sm-2">
                                添加时间
                            </th>
                            <th class="col-sm-1">
                                状态
                            </th>
                            <th class="col-sm-2">
                                操作
                            </th>
                        </div>
                    </tr>
                    <s:iterator value="#session.searchLetterlist" id="letter" status="st">
                        <tr <s:if test="#st.odd==false">
                            bgcolor="#eeeeee"
                            </s:if>
                            >
                            <td>
                                <s:property value="%{#st.count + ( #session.searchPageNum - 1 ) * @org.tju.wx.action.LetterAction@ELENUM}"
                                />
                            </td>
                            <td>
                                <s:property value="#letter.receiver" />
                            </td>
                            <td>
                                <s:if test="#letter.sender != ''">
                                    <s:property value="#letter.sender" />
                                </s:if>
                                <s:else>
                                    空
                                </s:else>
                            </td>
                            <td>
                                <s:if test="#letter.senderaddr!=''">
                                    <s:property value="#letter.senderaddr" />
                                </s:if>
                                <s:else>
                                    空
                                </s:else>
                            </td>
                            <td>
                                <s:property value="#letter.time" />
                            </td>
                            <td>
                                <s:if test="#letter.status==1">
                                    在团委
                                </s:if>
                                <s:else>
                                    已被取走
                                </s:else>
                            </td>
                            <td>
                                <s:if test="#letter.status==1">
                                    <a class="btn btn-primary btn-lg active" href="sendletter.action?key=<s:property value='#session.key'/>&preAction=search&letter.lid=<s:property value='#letter.lid'/>">
                                        取走
                                    </a>
                                </s:if>
                                <s:else>
                                    <a class="btn btn-primary btn-lg active" href="backletter.action?key=<s:property value='#session.key'/>&preAction=search&letter.lid=<s:property value='#letter.lid'/>">
                                        放回
                                    </a>
                                </s:else>
                                <a class="btn btn-primary btn-lg active" href="deleteletter.action?key=<s:property value='#session.key'/>&preAction=search&letter.lid=<s:property value='#letter.lid'/>">
                                    删除
                                </a>
                            </td>
                        </tr>
                    </s:iterator>
                </table>
                <div class="row">
                    <div align="left" class="col-sm-6">
                        <div class="message" id="actionmessage">
                            <font color="#000000">
                                <s:actionmessage/>
                            </font>
                            <font color="#ff0000">
                                <s:actionerror/>
                            </font>
                        </div>
                    </div>
                    <div align="right" class="col-sm-6">
                        <s:if test="#session.searchPageNum == 1">
                            <a class="btn btn-primary btn-lg active" disabled="disabled">
                                首页
                            </a>
                            &nbsp;&nbsp;&nbsp;
                            <a class="btn btn-primary btn-lg active" disabled="disabled">
                                上一页
                            </a>
                        </s:if>
                        <s:else>
                            <a class="btn btn-primary btn-lg active" href="searchletter.action?searchPageNum=1&key=<s:property value='#session.key'/>">
                                首页
                            </a>
                            &nbsp;&nbsp;&nbsp;
                            <a class="btn btn-primary btn-lg active" href="searchletter.action?searchPageNum=<s:property value='#session.searchPageNum - 1'/>&key=<s:property value='#session.key'/>">
                                上一页
                            </a>
                        </s:else>
                        <s:if test="#session.searchPageNum != #session.searchTotalpage">
                            <a class="btn btn-primary btn-lg active" href="searchletter.action?searchPageNum=<s:property value='#session.searchPageNum + 1'/>&key=<s:property value='#session.key'/>">
                                下一页
                            </a>
                            &nbsp;&nbsp;&nbsp;
                            <a class="btn btn-primary btn-lg active" href="searchletter.action?searchPageNum=<s:property value='#session.searchTotalpage'/>&key=<s:property value='#session.key'/>">
                                尾页
                            </a>
                        </s:if>
                        <s:else>
                            <a class="btn btn-primary btn-lg active" disabled="disabled">
                                下一页
                            </a>
                            &nbsp;&nbsp;&nbsp;
                            <a class="btn btn-primary btn-lg active" disabled="disabled">
                                尾页
                            </a>
                        </s:else>
                        <br/>
                        <font size="3px">
                            共
                            <font color="red">
                                <s:property value="#session.searchTotalpage" />
                            </font>
                            页
                        </font>
                        <font size="3px">
                            当前第
                            <font color="red">
                                <s:property value="#session.searchPageNum" />
                            </font>
                            页
                        </font>
                        <font size="3px">
                            共
                            <font color="red">
                                <s:property value="#session.searchCount" />
                            </font>
                            条记录
                            <br/>
                            <form action="searchletter" onSubmit="return validate();" method="get">
                                <font size="4">
                                    跳转至第
                                    <input type="text" name="searchPageNum" size="2" value="1" text-align:center/>
                                    页
                                    <input name="key" type="hidden" value="<s:property value='#session.key'/>"
                                    />
                                    <button class="btn btn-primary btn-lg active" type="submit">
                                        跳转
                                    </button>
                                </font>
                            </form>
                        </font>
                    </div>
                    <br/>
                </div>
            </s:else>
        </div>
    </body>
</html>