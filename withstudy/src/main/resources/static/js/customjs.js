var push = {
    init: function () {
        var _this = this;
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function (e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });

        let url = "/push";
        let eventSource = new EventSource(url);
        var push_data = {
            id: Number.MAX_SAFE_INTEGER,
            check: true
        };

        eventSource.onmessage = function (event) {
            $('#push-count').text(event.data);
            /*            $.ajax({
                            url: "/push",
                            type: "put",
                            success: function (result) {
                                $('#push-count').text(result);
                            }
                        });*/
        };
        eventSource.onerror = function () {
            if (eventSource == null) return;
            eventSource.close();
            eventSource = null;
        };

        $('#pushList').scroll(function () {
            if (push_data.check && $('#pushList').scrollTop() + $('#pushList').height() >= $('#pushList').prop('scrollHeight')) {
                _this.getPush(false, push_data);
            }
        });
        $('#pushBtn').click(function () {
            var _status = $('#pushBtn').attr('aria-expanded');
            if (_status == 'false')
                $('#pushBtn svg').attr('fill', 'rgba(255, 255, 255)');
            else
                $('#pushBtn svg').attr('fill', 'rgba(255, 255, 255, 0.5)');

            _this.getPush(true, push_data);
        });
    },
    getPush: function (isFirst, push_data) {
        $.ajax({
            url: "/pushList?size=" + 10 + '&lastId=' + push_data.id,
            type: "get",
            contentType: 'application/json; charset=utf-8',
            success: function (result) {
                var newTag = '';

                for (var i = 0; i < result.length; i++) {
                    newTag += '<a class="push-list dropdown-item border-bottom small';
                    if (result[i].read == true)
                        newTag += ' push-read" ';
                    else
                        newTag += '" ';
                    newTag += 'href="/push/' + result[i].id + '" style="height: 60px">';
                    newTag += '<strong>' + result[i].fromId + '</strong>';
                    newTag += result[i].message;
                    newTag += '<p class="text-right text">' + result[i].occurTime + '</p>';
                    newTag += '<input class="push-id end" type="hidden" value="' + result[i].id + '">';
                    newTag += '</a>';

                    push_data.id = Math.min(push_data.id, result[i].id);
                }
                if (result.length == 0)
                    push_data.check = false;
                else if (isFirst)
                    $('#pushList').html(newTag);
                else
                    $('#pushList').append(newTag);
            },
            error: function () {
                alert('오류 발생');
            }
        });
    }
}

push.init();