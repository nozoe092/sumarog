
$(function(){
	initializeHanbaitenListJsGrid();
});

/**
 * 検索ボタンクリック時の処理
 */
$(document).on('click', '#btnSearch', function () {
	// 入力エラーメッセージをクリア
	clearValidationErrorMessage();
	const formData = new FormData($('#hanbaitenListForm')[0]);
    sendAjaxRequest({
		url: '/hanbaiten/list/get-data',
		data:formData,
		onSuccess: function (response, responseData) {
			if(responseData.status == HTTP_STATUS.OK){
				setJsGridData('#hanbaitenListJsGrid', responseData.dataList);
				// 得意先コードの昇順にしておく
				$("#hanbaitenListJsGrid").jsGrid('sort', 'tokuisakiCode', 'asc');
			}else{
				setGlobalErrorMessage(responseData);
			}
		},
    });
	// submitさせない
	return false;
});


/**
 * グリッドの初期設定
 */
function initializeHanbaitenListJsGrid() {
    $("#hanbaitenListJsGrid").jsGrid({
		width: JS_GRID_DEFAULT_SETTINGS.PROPERTIES.WIDHT,
		height: JS_GRID_DEFAULT_SETTINGS.PROPERTIES.HEIGHT,
		inserting: JS_GRID_DEFAULT_SETTINGS.PROPERTIES.INSERTING,
		editing: JS_GRID_DEFAULT_SETTINGS.PROPERTIES.EDITING,
		sorting: JS_GRID_DEFAULT_SETTINGS.PROPERTIES.SORTING,
		paging: JS_GRID_DEFAULT_SETTINGS.PROPERTIES.PAGING,
		noDataContent: JS_GRID_DEFAULT_SETTINGS.PROPERTIES.NO_DATA_CONTENT,

		data: [],

		fields: [
			{ title: "得意先コード", name: "tokuisakiCode", type: "text", width: 180 },
			{ title: "得意先名", name: "tokuisakiName", type: "text", width: 180 },
			{ title: "得意先名カナ", name: "tokuisakiNameKana", type: "text", width: 280 },
			{ 
                title: "定型文メンテ", 
                type: "text", 
                itemTemplate: function(_, row) {

                    var $recruitmentNoticeLink = $("<a>")
                        .text("採用通知")
                        .addClass("btn btn-gird btn-outline-warning btn-xs")
                        .on("click", function() {
                            alert("採用通知: " + row.tokuisakiName);
                        });

					var $proofreadingLink = $("<a>")
                        .text("校正依頼")
                        .addClass("btn btn-gird btn-outline-secondary btn-xs")
                        .on("click", function() {
                            alert("校正依頼: " + row.tokuisakiNameKana);
                        });

                    return $("<div>").addClass("text-center grid-button-area").append($recruitmentNoticeLink).append($proofreadingLink);
                },
				width: 140,
				sorting: false,
            },
			{ 
                title: "詳細設定", 
                type: "text", 
                itemTemplate: function(_, row) {
					var $editLink = $("<a>")
                        .text("編集")
                        .addClass("btn btn-gird btn-success btn-xs")
                        .on("click", function() {
							window.open('/hanbaiten/detail?tokuisakiCode=' + row.encryptTokuisakiCode, '_blank');
                        });

                    return $("<div>").addClass("text-center grid-button-area").append($editLink);
                },
				width: 80,
				sorting: false,
            },
		]
	});
}
