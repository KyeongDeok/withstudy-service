var study = {
    init: function () {
        var _this = this;

        $('.studybtn').on('click', function () {
            _this.status(this);
        });
        $('#requestCheck').on('click', function () {
            _this.check();
        });
        $(document).on('click', '.accept-btn', function () {
            var _idx = $('.accept-btn').index(this);
            _this.request_process("수락", _idx, "put");
        });
        $(document).on('click', '.refuse-btn', function () {
            var _idx = $('.refuse-btn').index(this);
            _this.request_process("거절", _idx, "delete");
        });
    },

    status: function (cursor) {
        var msg = $(cursor).val();
        var roomNo = $('#roomNo').val();
        if (confirm(msg + "할까요?") == false) return;

        var url = $(cursor).attr('url');
        var method = $(cursor).attr('method');

        $.ajax({
            url: url,
            type: method,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(roomNo),
            success: function () {
                if (msg == '삭제') {
                    location.replace('/study');
                } else {
                    location.reload();
                }
            },
            error: function () {
                location.replace('/study');
            }
        });
    },

    check: function () {
        var data = $('#roomNo').val();
        $('.modal-body').html('');

        $.ajax({
            type: 'get',
            url: '/study/' + data + '/request',
            contentType: 'application/json; charset=utf-8',
            success: function (result) {
                var newTag = '<ul class="list-group">';
                for (var i = 0; i < result.length; i++) {
                    var name = result[i].name;
                    var uid = result[i].uid;
                    var picture = result[i].picture;

                    newTag += '<li class="request-list list-group-item py-1">';
                    newTag += '    <div class="row">';
                    newTag += '        <div class="col-2">';
                    newTag += '            <img class="img-thumbnail p-0" src="' + picture + '">';
                    newTag += '        </div>';
                    newTag += '        <div class="col-5 my-auto text-left">';
                    newTag += '            <h7 class="text-left">' + name + '</h7>';
                    newTag += '            <a href="/user/' + uid + '" class="stretched-link" target="_blank"></a>';
                    newTag += '        </div>';
                    newTag += '        <div class="col-5 my-auto text-right">';
                    newTag += '            <input type="hidden" class="request-id" value="' + uid + '">';
                    newTag += '            <input type="button" class="accept-btn btn btn-primary" value="Accept">';
                    newTag += '            <input type="button" class="refuse-btn btn btn-danger" value="No">';
                    newTag += '        </div>';
                    newTag += '    </div>';
                    newTag += '</li>';
                }
                newTag += '</ul>';
                $('#requestList').append(newTag);
                $('#requestTotal').text(result.length);
                $('#requestSize').text(result.length);
                $('#requestBox').modal('show');
            },
            error: function () {
                alert('오류가 발생하였습니다.');
                window.location.reload();
            }
        })
    },

    request_process: function (_msg, _idx, _method) {
        if (confirm("참여요청을 " + _msg + "할까요?") == false) return;
        var data = {uid: $('.request-id')[_idx].value};

        $.ajax({
            url: '/study/' + $('#roomNo').val() + '/request',
            type: _method,
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            success: function () {
                $('.request-list')[_idx].remove();
            },
            error: function () {
                alert("오류가 발생했습니다.");
                window.location.reload();
            }
        });
    }
};

study.init();