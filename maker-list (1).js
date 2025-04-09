$(function(){
	initializeMakerListJsGrid();

});

/**
 * 検索ボタンクリック時の処理
 */
$(document).on('click', '#btnSearch', function () {
	// 入力エラーメッセージをクリア
	clearValidationErrorMessage();
	const formData = new FormData($('#makerListForm')[0]);
    sendAjaxRequest({
		url: '/maker/list/get-data', // メーカー用のデータ取得エンドポイント
		data: formData,
		onSuccess: function (response, responseData) {
			if(responseData.status == HTTP_STATUS.OK){
				setJsGridData('#makerListJsGrid', responseData.dataList);
			}else{
				setGlobalErrorMessage(responseData);
			}
		},
    });
	// submitさせない
	return false;
});

 // 販売店セレクトボックス変更イベント
 $(document).on('change', '#hanbaitenSelectBox', function() {
	var selectedValue = $(this).val();
	changeSelectBoxHanbaiten('projectSelectBox', 'projectMakerSelectBox', selectedValue);
});

/**
 * グリッドの初期設定
 */
function initializeMakerListJsGrid() {
    $("#makerListJsGrid").jsGrid({
		width: JS_GRID_DEFAULT_SETTINGS.PROPERTIES.WIDHT,
		height: JS_GRID_DEFAULT_SETTINGS.PROPERTIES.HEIGHT,
		inserting: JS_GRID_DEFAULT_SETTINGS.PROPERTIES.INSERTING,
		editing: JS_GRID_DEFAULT_SETTINGS.PROPERTIES.EDITING,
		sorting: JS_GRID_DEFAULT_SETTINGS.PROPERTIES.SORTING,
		paging: JS_GRID_DEFAULT_SETTINGS.PROPERTIES.PAGING,
		noDataContent: JS_GRID_DEFAULT_SETTINGS.PROPERTIES.NO_DATA_CONTENT,

		data: [],

		fields: [
			{ title: "コード", name: "tokuisakiCode", type: "text", width: 180 },
			{ title: "名称", name: "tokuisakiName", type: "text", width: 180 },
			{ title: "カテゴリー", name: "category", type: "text", width: 200 },
			{ 
                title: "詳細設定", 
                type: "text", 
                itemTemplate: function(_, row) {
					var $editLink = $("<a>")
                        .text("編集")
                        .addClass("btn btn-gird btn-success btn-xs")
                        .on("click", function() {
							window.open('/maker/detail?tokuisakiCode=' + row.tokuisakiCode, '_blank');
                        });

                    return $("<div>").addClass("text-center grid-button-area").append($editLink);
                },
				width: 80,
            },
		]
	});
}
