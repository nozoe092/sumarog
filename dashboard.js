var stompClient = null;

$(function(){
	//alert("Hello, World!");
	connectWebSocket();

	$("#testDuallistbox").bootstrapDualListbox({
		infoText: '',
	});
});

/**
 * ボタンクリック時の処理
 */
$(document).on('click', '#btnTest', function () {
	var maxCount = Math.max(...$('#addTestArea .input-test-area').map(function () {
		return $(this).data('count');
	}).get());
	
	// 要素が一つもない場合
	if(!isFinite(maxCount)){
		maxCount = 0;	
	}
	
	// 追加する要素領域
	var element = $('#addTestArea');
    sendAjaxRequest({
		url: '/dashboard/get-test-list',
		method: 'POST',
		data:JSON.stringify({ test: 'hello', count: maxCount }),
		onSuccess: function (response, responseData) {
			element.append(responseData);
		},
		onError: function (response, responseData) {
			console.log('エラー：'+responseData.responseText);
		}
    });
});

/**
 * 削除ボタンクリック時の処理
 */
$(document).on('click', '.btn-test-delete', function () {
    var count = $(this).data('count'); 
    $('#inputTestArea_' + count).remove();
});

/**
 * 販売店セレクトボックス変更イベント
 */
$(document).on('change', '#hanbaitenSelectBox', function () {
	var selectedValue = $(this).val();
	changeSelectBoxHanbaiten('projectSelectBox', 'projectMakerSelectBox', selectedValue);
});

/**
 * 企画セレクトボックス変更イベント
 */
$(document).on('change', '#projectSelectBox', function () {
	var selectedValue = $(this).val();
	var tokuisakiCode = $('#hanbaitenSelectBox').val();
	changeSelectBoxProject('projectMakerSelectBox', tokuisakiCode, selectedValue);
});

/**
 * ソケット送信ボタンクリック時の処理
 */
$(document).on('click', '#btnSendChatMessage', function () {
	// サーバーに送信するメッセージ
	var message = $('#chatMessage').val();
	stompClient.send("/app/send-chat-message", {}, JSON.stringify({'message': message}));
});

/**
 * websocket接続
 */
function connectWebSocket() {
    var socket = new SockJS('/receive-chat-message');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/receive/chat-message', function (result) {
			// サーバーから受信したメッセージを表示
			var chatMessage = JSON.parse(result.body);
			var tokuisakiName = chatMessage.tokuisakiName;
			var message = chatMessage.message;
            $("#chatMessageOutputArea").append("<tr><td style='width: 280px;'>" + tokuisakiName + "</td><td>" + message + "</td></tr>");
        });
    });
}
