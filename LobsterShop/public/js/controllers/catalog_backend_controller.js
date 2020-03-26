/**
 * Created by yanglu on 15/11/16.
 */

var app = angular.module('CatalogBackendApp', ['ngGrid', 'angularFileUpload', 'fundoo.services']);
var cellEditableTemplate = "<input ng-class=\"'colt' + col.index\" ng-input=\"COL_FIELD\" ng-model=\"COL_FIELD\" ng-blur=\"updateEntity(col, row)\"/>";
var uploadTemplateCatalog = '<div> <input type="file" name="files[]" accept="image/*" ng-file-select="uploadImage($files, col.field, row.entity)"/>   <a class="fancybox" target="_blank" data-fancybox-group="gallery" fancybox ng-if="isShowImg(COL_FIELD)" ng-href="/showImage/{{COL_FIELD}}"><img ng-src="/showImage/{{COL_FIELD}}" style="width:120px;height:80px" ></a></div>';
var uploadTemplateCatalogSmallIamge = '<div> <input type="file" name="files[]" accept="image/*" ng-file-select="uploadImage($files, col.field, row.entity)"/>   <a class="fancybox" target="_blank" data-fancybox-group="gallery" fancybox ng-if="isShowImg(COL_FIELD)" ng-href="/showImage/{{COL_FIELD}}"><img ng-src="/showImage/{{COL_FIELD}}" style="width:80px;height:80px" ></a></div>';

app.filter('safehtml', function($sce) {
    return function(htmlString) {
        return $sce.trustAsHtml(htmlString);
    }
});

app.controller('CatalogBackendController', ['$scope', '$http', '$upload', 'createDialog', '$log', function ($scope, $http, $upload, createDialogService, $log) {
	
    $scope.currentObj = {}
    $scope.list = []
    $scope.page = 1;
    $scope.pageInfo = {}

    $scope.$watch('page', function(){
        refreshDate();
    }, false);
    
    $scope.goHomePage = function() {
    	$scope.page = 1;
    }
    
    $scope.goPrevPage = function() {
    	$scope.page = $scope.pageInfo.current -1;
    }

    $scope.goNextPage = function() {
        $scope.page = $scope.pageInfo.current +1;
    }
    
    $scope.goLastPage = function() {
    	$scope.page = $scope.pageInfo.total;
    }

    $scope.goJumpPage = function() {
        if($scope.jumpPage > $scope.pageInfo.total){
            $scope.jumpPage = $scope.pageInfo.current
            bootbox.alert('总页数最多为' +$scope.pageInfo.total+ '页');
        }else{
            $scope.page = $scope.jumpPage;
        }
    }

    function refreshDate(){
        $http.get('/catalogs?page=' + $scope.page).success(function (data, status, headers, config) {
        	$log.log(data)
            if (data.flag) {
                $scope.list = data.data;
                $scope.pageInfo = data.page;
            }
            else {
            	bootbox.alert(data.message)
            }
        });
    }
    
    $scope.gridCatalog = { data: 'list',
        rowHeight: 100,
        // showSelectionCheckbox:true,
        // enableCellSelection: false,
        enableRowSelection: true,
        selectedItems: [],
        multiSelect:false,
        // enableCellEdit: false,
        plugins:[new ngGridFlexibleHeightPlugin()],
        columnDefs: [
            {field: 'id', displayName: 'Id', width: '5%'},
            {field: 'catalogIndex', displayName: '顺序', width: '5%', enableCellEdit: true, editableCellTemplate: cellEditableTemplate},
            {field: 'name', displayName: '名称', width: '15%', enableCellEdit: true, editableCellTemplate: cellEditableTemplate},
            {field: 'images', displayName: '大图', cellTemplate: uploadTemplateCatalog, width: '25%'},
            {field: 'smallImages', displayName: '小图片', cellTemplate: uploadTemplateCatalogSmallIamge, width: '25%'},
            {field: 'productNum', displayName: '产品数', width: '8%'},
            {field: 'comment', displayName: '备注', width: '15%', enableCellEdit: true, editableCellTemplate: cellEditableTemplate},
        ] };

	$scope.uploadImage = function($files, imageField, parentObj) {
        for (var i = 0; i < $files.length; i++) {
    		var file = $files[i];
    		
    		$log.log('start upload image file on id: '
    				+ parentObj.id + ', file: ' + file
    				+ ', property: ' + imageField)
    				
    		$scope.upload = $upload.upload({
    			url : '/upload/image',
    			data : {
    				cid : parentObj.id,
    				className : 'CatalogModel',
    				property : imageField
    			},
    			file : file
    		})
    				.progress(
    						function(evt) {
    							$log.log('upload percent: '
    									+ parseInt(100.0 * evt.loaded
    											/ evt.total));
    						})
    				.success(function(data, status, headers, config) {
    					$log.log(data);
    					if (data.flag) {
    						if (imageField == 'images') {
    							parentObj.images = data.data;
    						} else if (imageField == 'smallImages') {
    							parentObj.smallImages = data.data;
    						} else {
    							bootbox.alert('字段不存在: ' + property)
    						}
    					} else {
    						bootbox.alert(data.message)
    					}
    				});
    		// .error(...)
    		// .then(success, error, progress);
        }
	};

	$scope.isShowImg = function(url) {
		return (url) && (url.length > 0);
	};
	
    // 当前行更新字段
    $scope.updateEntity = function(column, row) {
        $scope.currentObj = row.entity;
        $scope.saveContent();
    };

    // 新建或更新对象
    $scope.saveContent = function() {
        var content = $scope.currentObj;
        var isNew = !content.id
        var url = '/catalogs'
        if(isNew){
        	var http_method = "POST";
        }else{
        	var http_method = "PUT";
        	url += '/' + content.id
            var pos = $scope.list.indexOf(content);
        }
        $http({method: http_method, url: url, data:content}).success(function(data, status, headers, config) {
                if(data.flag){
                    if(isNew){
                        $scope.list.push(data.data);
                        bootbox.alert('新建[' + data.data.name + ']成功');
                    }else{
                        $scope.list[pos] = data.data;
                    }
                }else{
                    bootbox.alert(data.message);
                }
            });
    };

    $scope.deleteContent = function(){
        var items = $scope.gridCatalog.selectedItems;
        if(items.length == 0){
            bootbox.alert("请至少选择一个对象.");
        }else{
            var content = items[0];
            if(content.id){
                bootbox.confirm("您确定要删除这个对象[" + content.name + "]吗?", function(result) {
                    if(result) {
                        $http.delete('/catalogs/' + content.id).success(function(data, status, headers, config) {
                            if (data.flag) {
                            	var index = $scope.list.indexOf(content);
                                $scope.gridCatalog.selectItem(index, false);
                                $scope.list.splice(index, 1);
                                bootbox.alert("删除成功");
                            }
                            else {
                                bootbox.alert(data.message);
                            }
                        });
                    }
                });
            }
        }
    };

    $scope.formSave = function(formOk){
    	if(!formOk){
            bootbox.alert('验证有误, 请重试');
            return
    	}
        $scope.saveContent();
        $scope.$modalClose();
    };
    
    $scope.addContent = function(){
        $scope.currentObj = {};
        createDialogService("/assets/js/controllers/catalog_editortemplate.html",{
            id: 'editor',
            title: '新建主题',
            scope: $scope,
            footerTemplate: '<div></div>'
        });
    };
}]);
