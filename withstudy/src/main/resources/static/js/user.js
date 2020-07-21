var user = {
    init: function () {
        var _this = this;
        // 저정
        $('#port-save').on('click', function () {
            _this.save();
        });
        // 삭제
        $(document).on('click', '.port-delete', function () {
            var idx = $('.port-delete').index(this);
            var seq = $(this).attr('seq');
            _this.delete(idx, seq);
        });
        $('#nickname-update').on('click', function () {
            _this.nameUpdate();
        });
        $('#change-btn').click(function () {
            $("#server_msg").html("");
            $("#to_nickname").val("");
            $("#to_nickname").focus();
        });
    },
    save: function () {
        var data = {
            name: $('#port_name').val().trim(),
            url: $('#port_url').val().trim()
        }

        if (data.name.length == 0) {
            alert('라벨을 입력해주세요.');
            $('#port_name').focus();
            return;
        }
        if (data.url.length == 0) {
            alert('url을 입력해주세요.');
            $('#port_url').focus();
            return;
        }
        $.ajax({
            type: 'post',
            url: '/user/portfolio',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            success: function (result) {
                var newTag = '';
                newTag += '<span class="port-list badge badge-secondary mr-2 mb-2">';
                newTag += '    <a class="btn text-white mr-2" target="_blank" href="' + data.url + '">' + data.name + '</a>';
                newTag += '    <a class="port-delete btn btn-dark text-white" seq="' + result + '">&times;</a>';
                newTag += '</span>';

                $('#portfolio_section').append(newTag);
                $('#port_name').val("");
                $('#port_url').val("http://");
                $('#port_name').focus();
            },
            error: function (e) {
                alert('오류가 발생하였습니다.');
            }
        });
    },
    update: function () {

    },
    delete: function (idx, seq) {
        if (confirm("삭제할까요?") == false) return;
        var data = {id: seq};

        $.ajax({
            type: 'delete',
            url: "/user/portfolio",
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            success: function () {
                $('.port-list')[idx].remove();
            },
            error: function (e) {
                alert(JSON.stringify(e));
            }
        });
    },
    nameUpdate: function () {
        var toname = $("#to_nickname").val().trim();
        if (toname.length < 4) {
            $("#server_msg").attr('class', 'col-form-label text-danger');
            $("#server_msg").html("닉네임은 4글자 이상으로 만들어주세요.");
            return;
        }
        $.ajax({
            url: '/user/change',
            type: 'POST',
            data: {"toname": $("#to_nickname").val()},
            success: function (msg) {
                if (msg == 1) {
                    $("#server_msg").attr('class', 'col-form-label text-danger');
                    $("#server_msg").html("이미 사용중인 닉네임입니다.");
                } else {
                    alert("닉네임 변경이 완료되었습니다.");
                    location.reload();
                }
            },
            error: function (e) {
                $("#server_msg").html("오류가 발생하였습니다.");
            }
        });
    }
};

user.init();