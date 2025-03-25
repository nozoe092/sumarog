
/**
 * 追加ボタンクリック時の処理
 */
$(document).on('click', '#btnAddTantoushaForm', function () {
	var maxId = Math.max(...$('#addTantoushaFormArea .tantousha-form-area').map(function () {
		return $(this).data('id');
	}).get());
	
	// 要素が一つもない場合
	if(!isFinite(maxId)){
		maxId = 0;	
	}
	
	// 追加する要素領域
	var element = $('#addTantoushaFormArea');
    sendAjaxRequest({
		url: '/hanbaiten/detail/get-tantousha-form',
		method: 'POST',
		data:JSON.stringify({ maxId: maxId }),
		onSuccess: function (response, responseData) {
			if(responseData != null && responseData.status == HTTP_STATUS.OK){
				element.append(responseData.html);
			}else{
				setGlobalErrorMessage(responseData);
			}
		},
    });
});

/**
 * 削除ボタンクリック時の処理
 */
$(document).on('click', '.btn-delete-tantousha-form', function () {
    var id = $(this).data('id'); 
    $(`#tantoushaFormArea_${id}`).remove();
	return false;
});

/**
 * 認証コード送信ボタンクリック
 */
$(document).on('click', '.btn-send-auth-code', function () {
	clearValidationErrorMessage();
	var id = $(this).data('id'); 
	var mailAddress = $(`[id='tantoushaFormList${id}.mailAddress']`).val();
    sendAjaxRequest({
		url: '/hanbaiten/detail/send-auth-code',
		method: 'POST',
		data:JSON.stringify({ mailAddress: mailAddress }),
		onSuccess: function (response, responseData) {
			if(responseData.status == HTTP_STATUS.OK){
				setGlobalSuccessMessage();
			}else{
				setGlobalErrorMessage(responseData);
			}
		},
    });
});

/**
 * 保存ボタンクリック時の処理
 */
$(document).on('click', '#btnSave', function () {
	// 入力エラーメッセージをクリア
	clearValidationErrorMessage();
	const formData = new FormData($('#hanbaitenDetailForm')[0]);
    sendAjaxRequest({
		url: '/hanbaiten/detail/save',
		data:formData,
		onSuccess: function (response, responseData) {
			if(responseData != null && responseData.status == HTTP_STATUS.OK){
				// 保存成功メッセージを表示
				saveGlobalSuccessMessageForStorage(responseData.message);
				window.location.reload();
			}else{
				setGlobalErrorMessage(responseData);
			}
		},
    });
	// submitさせない
	return false;
});

/**
 * 閉じるボタンクリック
 */
$(document).on('click', '#btnClose', function () {
	window.close();
});