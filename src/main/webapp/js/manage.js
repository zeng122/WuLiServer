jQuery(document).ready(function($) {
    $.myFunAgree = function(jubaoId , blogId){
        $.ajax({
                type: "POST",
                data: {
                    jubaoId: jubaoId,
                    blogId: blogId
                },
                url: "/blog/agreeJubao.do",
                dataType: "text",
                success: function (res) {
                    console.log(res);
                    var json = JSON.parse(res);
                    var judge = json.state;
                    if (judge == "true") {
                        alert("处理成功")
                    } else {
                        alert("处理失败")
                    }
                }
            }
        );
    }

    $.myFunRefuse = function(){
        $.ajax({
                type: "POST",
                data: {
                    jubaoId: jubaoId,
                },
                url: "/blog/refuseJubao.do",
                dataType: "text",
                success: function (res) {
                    console.log(res);
                    var json = JSON.parse(res);
                    var judge = json.state;
                    if (judge == "true") {
                        alert("处理成功")
                    } else {
                        alert("处理失败")
                    }
                }
            }
        );
    }

    $.ajax({
            type: "POST",
            url: "/blog/getAllJubao.do",
            dataType: "text",
            success: function (res) {
                console.log(res);
                var json = JSON.parse(res);
                $.each(json, function (index, values) {
                    var ElementID = "<td>" + values.id + "</td>";
                    var ElementBlogID = "<td>" + values.blogid + "</td>";
                    var ElementUserID = "<td>" + values.uid + "</td>";
                    var ElementName = "<td>" + values.name + "</td>";
                    var ElementReason = "<td>" + values.reason + "</td>";
                    var ElementContend = "<td>" + values.blogContend + "</td>";
                    var ElementBtCon = "<td align='center'><button style=' width: 50px' onclick='$.myFunAgree("+values.id+","+values.blogid+")'>确定</button></td>";
                    var ElementBtRef = "<td align='center'><button style=' width: 50px' onclick='$.myFunRefuse("+values.id+")'>确定</button></td>";
                    var ElementTr = "<tr>" + ElementID + ElementBlogID + ElementUserID +
                        ElementName+ElementContend+ElementReason+ElementBtCon+ElementBtRef+"</tr>";
                    $(".jubao").append(ElementTr);
                });
            }
        }
    );

});