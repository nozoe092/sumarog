
/**
 * 追加ボタンクリック時の処理
 */
$(document).on('click', '#btnAddTantoushaForm', function () {
	var maxIndex = Math.max(...$('#addTantoushaFormArea .tantousha-form-area').map(function () {
		return $(this).data('index');
	}).get());
	
	// 要素が一つもない場合
	if(!isFinite(maxIndex)){
		maxIndex = 0;	
	}

	// 最大担当者数
	var maxTantoushaListIndex = parseInt($('#maxTantoushaListIndex').val());
	// 現在の担当者数
	var elementCount = $('#addTantoushaFormArea .tantousha-form-area').length;

	if(elementCount < maxTantoushaListIndex){
		// 追加する
		var element = $('#addTantoushaFormArea');
		sendAjaxRequest({
			url: '/maker/detail/get-tantousha-form',
			method: 'POST',
			data:JSON.stringify({ maxIndex: maxIndex }),
			onSuccess: function (response, responseData) {
				if(responseData != null && responseData.status == HTTP_STATUS.OK){
					element.append(responseData.html);
				}else{
					setGlobalErrorMessage(responseData);
				}
			},
		});
	}else{
		showWarningDialogMessage(MESSAGE.TITLE.NOTICE, `追加できる担当者は最大 ${maxTantoushaListIndex} 名です`);
	}
});

/**
 * 削除ボタンクリック時の処理
 */
$(document).on('click', '.btn-delete-tantousha-form', function () {
    var index = $(this).data('index'); 
    $(`#tantoushaFormArea_${index}`).remove();
	return false;
});

/**
 * 認証コード送信ボタンクリック
 */
$(document).on('click', '.btn-send-auth-code', function () {
	showConfirmDialogMessage(MESSAGE.TITLE.CONFIRM, '認証コードを送信しますか？', null, sendAuthCode);
});

/**
 * 認証コード送信
 */
function sendAuthCode(){
	clearValidationErrorMessage();
	var id = $(this).data('id'); 
	var mailAddress = $(`[id='tantoushaFormList${id}.mailAddress']`).val();
    sendAjaxRequest({
		url: '/maker/detail/send-auth-code',
		method: 'POST',
		data:JSON.stringify({ mailAddress: mailAddress }),
		onSuccess: function (response, responseData) {
			if(responseData.status == HTTP_STATUS.OK){
				showSuccessDialogMessage(MESSAGE.TITLE.NOTICE, responseData.message);
			}else{
				setGlobalErrorMessage(responseData);
			}
		},
    });
}

/**
 * 保存ボタンクリック時の処理
 */
$(document).on('click', '#btnSave', function () {
	showConfirmDialogMessage(MESSAGE.TITLE.CONFIRM, MESSAGE.CONFIRM_SAVE, null, save);
	// submitさせない
	return false;
});

/**
 * 保存する
 */
function save(){
	// 入力エラーメッセージをクリア
	clearValidationErrorMessage();
	const formData = new FormData($('#makerDetailForm')[0]);
	sendAjaxRequest({
		url: '/maker/detail/save',
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
}

/**
 * 閉じるボタンクリック
 */
$(document).on('click', '#btnClose', function () {
	window.close();
});