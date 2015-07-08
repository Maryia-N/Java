$(function() {
	localization.init();
	windowHistory.init();
	news.getListNews.action();

	$(document).on("click", "#lang-ru", function() {
		localization.translate(localization.ru);
	});
	$(document).on("click", "#lang-en", function() {
		localization.translate(localization.en);
	});

});

var request = {

	dataType : 'json',
	contentType : 'application/json',

	type : {
		GET : 'GET',
		POST : 'POST',
		PUT : 'PUT',
		DELETE : 'DELETE'
	},

	url : {
		newsDefault : 'task/news/',
		newsByFilter : 'task/news?',
		commentsDefault : 'task/comments/',
		commentsByFilter : 'task/comments?',
		tagsDefault : 'task/tags/',
		tagsByFilter : 'task/tags?',
		authorsDefault : 'task/authors/',
		authorsByFilter : 'task/authors?'
	},

	send : function(path, dataRequest, typeRequest, successFunction) {
		var dataRequets = $.ajax({
			url : path,
			data : dataRequest,
			type : typeRequest,
			contentType : this.contentType,
			timeout : 30000
		}).done(successFunction).fail(function(jqXHR, textStatus) {
			switch (jqXHR.status) {
			case 400:
				message.getMessage(i18n.t("content.bad_request"));
				break;
			case 404:
				message.getMessage(i18n.t("content.not_found"));
				break;
			case 405:
				message.getMessage(i18n.t("content.not_allowed"));
				break;
			case 409:
				message.getMessage(i18n.t("content.conflict"));
				break;
			case 422:
				message.getMessage(i18n.t("content.unprocessable_entity"));
				break;
			case 500:
				message.getMessage(i18n.t("content.server_error"));
				break;
			case 503:
				message.getMessage(i18n.t("content.service_unavailable"));
				break;
			default:
				message.getMessage(i18n.t("content.another_error"));
				break;
			}
			switch (textStatus) {
			case 'parsererror':
				message.getMessage(i18n.t("content.json_parse_failed"));
				break;
			case 'timeout':
				message.getMessage(i18n.t("content.timeout_exception"));
				break;
			case 'abort':
				message.getMessage(i18n.t("content.abort_exception"));
				break;
			}
		});
	}
}

var news = {

	filter : {
		page : 1,
		size : 4,
		tagName : null,
		authorId : null,
		top : null,

		setPage : function(page) {
			if (page <= 1) {
				this.page = 1;
			} else {
				this.page = page;
			}
		},

		getPage : function() {
			return this.page;
		},

		setSize : function(size) {
			if (size <= 4) {
				this.size = 4;
			} else {
				this.size = size;
			}
		},

		getSize : function() {
			return this.size;
		},

		setFilter : function(page, tagName, authorId, top) {
			this.setPage(page);
			this.tagName = tagName;
			this.authorId = authorId;
			this.top = top;
		},

		getFilter : function() {
			var currentFilter = news.filter.getCurrentState();
			return $.param(currentFilter);
		},

		getCurrentState : function() {
			var filter = {};
			if (this.page != null)
				filter["offset"] = (this.page == null || this.page < 1) ? 0
						: (this.page - 1) * this.size;
			if (this.size != null)
				filter["size"] = this.size;
			if (this.tagName != null)
				filter["tagName"] = this.tagName;
			if (this.authorId != null)
				filter["authorId"] = this.authorId;
			if (this.top != null)
				filter["top"] = this.top
			return filter;
		}
	},

	getListNews : {
		key : 'get-list-news',
		action : function() {
			var params = news.filter.getFilter();
			var path = request.url.newsByFilter + params;
			request.send(path, null, request.type.GET, function(response) {
				template.getListNews(response);
				template.getNewsPagination();
				if($('#sorted-menu').length == 0){
					var menu = new menuParameters();
					menu.addFormView = true;
					menu.sortedMenu = true;
					template.getMenu(menu);	
				}
				windowHistory.addState({
					key: news.getListNews.key
				});			
			});
		}
	},

	getSingleNews : {
		key : 'get-single-news',
		action : function(newsId) {
			var path = request.url.newsDefault + newsId;
			request.send(path, null, request.type.GET, function(response) {
				template.getSingleNews(response);
				var menu = new menuParameters();
				menu.newsDeleteView = true;
				menu.addFormView = true;
				menu.editFormView = true;
				menu.backView = true;
				menu.data = response;
				template.getMenu(menu);	
				comment.filter.setPage(1);
				comment.getListCommentsByNewsId.action(response.newsId);
				windowHistory.addState({
					key: news.getSingleNews.key,
					data: newsId
				});
			});
		}
	},
	
	deleteNews : {
		key : 'delete-news',
		action : function(newsId) {
			var path = request.url.newsDefault + newsId;
			message.getConfirm(null, i18n.t("content.askDeleteNews"), function(answer) {
				if (answer) {
					request.send(path, null, request.type.DELETE, function() {
						news.getListNews.action();
					});
				}
			});
		}
	},
	
	addNews:{
		key:'add-news',
		action:function(requestData){
			var data = JSON.stringify(requestData);
			var path = request.url.newsDefault;
			request.send(path, data, request.type.POST, function(response) {
				news.getSingleNews.action(response.newsId);
			});
		}
	},
	
	editNews:{
		key:'add-news',
		action:function(requestData){
			var data = JSON.stringify(requestData);
			var path = request.url.newsDefault + requestData.newsId;
			request.send(path, data, request.type.PUT, function(response) {
				news.getSingleNews.action(response.newsId);
			});
		}
	},
	
	openAddForm: {
		key:'open-add-news-form',
		action:function(){	
			var menu = new menuParameters();
			menu.newsListView = true;
			menu.backView = true;
			menu.editMenu = true;
			template.getMenu(menu);	
			template.getAddForm();
			windowHistory.addState({
				key: news.openAddForm.key
			});
		}
	},
	
	openEditForm:{
		key:'open-edit-news-form',
		action:function(newsId){
			var path = request.url.newsDefault + newsId;
			request.send(path, null, request.type.GET, function(response) {		
				var menu = new menuParameters();
				menu.newsListView = true;
				menu.backView = true;
				menu.editMenu = true;				
				menu.data = response;
				template.getMenu(menu);	
				template.getEditForm(response);
				windowHistory.addState({
					key: news.openEditForm.key,
					data: newsId
				});
			});			
		}
	},
	
	getDataFromForm: function(){
		var requestData = {};
		var newsId = $('#newsId').val();
		var title = $('#title').val().trim();
		var shortText = $('#shortText').val().trim();
		var fullText = $('#fullText').val().trim();
		if(validation.isValidNews(title, shortText, fullText)){			
			if(newsId!==''){
				requestData["newsId"] = newsId;				
			}
			requestData["title"] = title;
			requestData["shortText"] = shortText;
			requestData["fullText"] = fullText;
			requestData["tags"] = tag.getTagsFromForm();
			requestData["authors"] = author.getAuthorsFromForm();
			return requestData;
		}
		return null;
	},
	checkForm : {
		title : '',
		shortText : '',
		fullText : '',
		tags : [],
		authors : [],

		setOriginalState : function(title, shortText, fullText) {
			this.title = title;
			this.shortText = shortText;
			this.fullText = fullText;
		},
		
		setTags: function(tags){
			this.tags = tags;
		},
		
		setAuthors : function(authors) {
			this.authors = authors;
		},

		isChanged : function() {
			if (window.history.state !== null
					&& (window.history.state.key === news.openEditForm.key 
							|| window.history.state.key === news.openAddForm.key)) {
				var title = $('#title').val().trim();
				var shortText = $('#shortText').val().trim();
				var fullText = $('#fullText').val().trim();
				var tags = tag.getTagsFromForm();
				var authors = author.getAuthorsFromForm();
				if (this.title !== title)
					return true;
				if (this.shortText !== shortText)
					return true;
				if (this.fullText !== fullText)
					return true;
				if (this.tags.length !== tags.length)
					return true;
				if (this.authors.length !== authors.length)
					return true;
				for (item in tags) {
					if (!utils.contains(this.tags, tags[item]))
						return true;
				}
				for (item in authors) {
					if (!utils.contains(this.authors, authors[item]))
						return true;
				}
				return false;
			}
			return false;
		}
	}
}

var tag = {
	getTags : function(successFunction) {
		var path = request.url.tagsDefault;
		request.send(path, null, request.type.GET, successFunction);
	},
	getTagsCount : function() {
		var path = request.url.tagsByFilter + 'count';
		request.send(path, null, request.type.GET, function(response) {
			template.loadTagsToSortedMenu(response);
		});
	},
	addTag : function(requestData) {
		var data = JSON.stringify(requestData);
		var path = request.url.tagsDefault;
		request.send(path, data, request.type.POST, function(response) {
			template.loadTagToMenu(response);
			$('.text-tag').val(null);
		});
	},
	getTagsFromForm: function(){
		var tags = [];
		$("input.tags-checkbox:checked").each(function() {
			tags.push({"tagName": this.value});
		});
		return tags;
	}
}

var author = {
	getAuthors : function(successFunction) {
		var path = request.url.authorsDefault;
		request.send(path, null, request.type.GET, successFunction);
	},
	getAuthorCount : function() {
		var path = request.url.authorsByFilter + 'count';
		request.send(path, null, request.type.GET, function(response) {
			template.loadAuthorsToSortedMenu(response);
		});
	},
	addAuthor : function(requestData) {
		var data = JSON.stringify(requestData);
		var path = request.url.authorsDefault;
		request.send(path, data, request.type.POST, function(response) {
			template.loadAuthorToMenu(response);
			$('.text-author').val(null);
		});
	},
	getAuthorsFromForm: function(){
		var authors = [];
		$("input.authors-checkbox:checked").each(function() {
			authors.push({"authorId": this.value});
		});
		return authors;
	}
}

var comment = {

	filter : {
		page : 1,
		size : 2,

		setPage : function(page) {
			if (page <= 1) {
				this.page = 1;
			} else {
				this.page = page;
			}
		},

		getPage : function() {
			return this.page;
		},

		setSize : function(size) {
			if (size <= 2) {
				this.size = 2;
			} else {
				this.size = size;
			}
		},

		getSize : function() {
			return this.size;
		},

		getCurrentState : function(newsId) {
			var filter = {};
			if (this.page != null)
				filter["offset"] = (this.page == null || this.page < 1) ? 0
						: (this.page - 1) * this.size;
			if (this.size != null)
				filter["size"] = this.size;
			if (newsId != null)
				filter["newsId"] = newsId;
			return filter;
		},

		getFilter : function(newsId) {
			return $.param(this.getCurrentState(newsId));
		}
	},

	getListCommentsByNewsId : {
		key : 'get-list-comments-by-id',
		action : function(newsId) {
			var params = comment.filter.getFilter(newsId);
			var path = request.url.commentsByFilter + params;
			request.send(path, null, request.type.GET, function(response) {
				template.getCommentsList(response);
				template.getCommentsPagination(newsId);
			});
		}
	},

		
	addComment : {
		key : 'add-comment',
		action : function(requestData) {
			var data = JSON.stringify(requestData);
			var path = request.url.commentsDefault;
			request.send(path, data, request.type.POST, function(response) {
				comment.getListCommentsByNewsId.action(requestData.newsId);
				$('.new-comment-text').val('');
			});
		}
	},

	deleteCommentById : {
		key : 'delete-comment',
		action : function(commentId, newsId) {
			var path = request.url.commentsDefault + commentId;
			message.getConfirm(null, i18n.t("content.askDeleteComment"), function(answer){
				if(answer){					
					request.send(path, null, request.type.DELETE, function(){
						comment.getListCommentsByNewsId.action(newsId);
					});
				}
			});
		}
	}
}

var windowHistory = {
	init : function() {
		window.onpopstate = function(event) {
			if (window.history.state != null
					&& window.history.state.key !== null) {
				$.each(news, function(idx, value) {
					if (value.key != undefined
							&& value.key === window.history.state.key) {
						window[value.action((window.history.state.data))];
						return false;
					}
				});
			}
		};
	},

	addState : function(state, data) {
		if (windowHistory.checkCurrentState(state)) {
			window.history.pushState(state, data);
		}
	},

	checkCurrentState : function(state) {
		if (window.history.state === null)
			return true;
		if (window.history.state.key !== state.key)
			return true;
		return false;
	},

	compareKey : function(key) {
		if (window.history.state === null)
			return false;
		return (window.history.state.key !== key) ? false : true;
	},

	goBack : function() {
		history.back();
	}
}

var localization = {
	ru : 'ru',
	en : 'en',

	path : 'resources/locales/__lng__/__ns__.json',
	namespace : 'translation',

	init : function() {
		i18n.init({
			lng : this.en,
			ns : this.namespace,
			resGetPath : this.path
		}, function() {
			$("*").i18n();
		});
	},

	translate : function(language) {
		i18n.setLng(language, function() {
			$("*").i18n();
		});
	},

	update : function(place) {
		var currentLanguage = i18n.options.lng;
		i18n.setLng(currentLanguage, function() {
			$(place).i18n();
		});
	}
}

var validation = {

	isValidComment : function(commentText) {
		var text = commentText.trim();
		if (text == null || text == "") {
			message.getMessage(i18n.t("content.no_valid_comment_message"));
			return false;
		} else {
			return true;
		}
	},

	isValidNews : function(title, shortText, fullText) {
		var title = title.trim();
		var shortText = shortText.trim();
		var fullText = fullText.trim();

		var flag = true;
		if (title == null || title == "") {
			message.getMessage(i18n.t("content.no_valid_title_message"));
			flag = false;
		}
		if (shortText == null || shortText == "") {
			message.getMessage(i18n.t("content.no_valid_short_text_message"));
			flag = false;
		}
		if (fullText == null || fullText == "") {
			message.getMessage(i18n.t("content.no_valid_full_text_message"));
			flag = false;
		}
		return flag;
	},

	isValidTag : function(tagName) {
		var text = tagName.trim();
		if (text == null || text == "") {
			message.getMessage(i18n.t("content.no_valid_tag_message"));
			return false;
		} else {
			return true;
		}
	},

	isValidAuthor : function(name) {
		var text = name.trim();
		if (text == null || text == "") {
			message.getMessage(i18n.t("content.no_valid_author_message"));
			return false;
		} else {
			return true;
		}
	}
}

var message = {

	path : 'resources/js/lib/message.js',

	getAlert : function(titleMessage, message) {
		$.getScript(this.path, function() {
			dhtmlx.alert({
				title : titleMessage,
				text : message
			});
		})
	},

	getConfirm : function(titleMessage, message, callback) {
		$.getScript(this.path, function() {
			dhtmlx.confirm({
				title : titleMessage,
				text : message,
				callback : callback
			})
		})
	},

	getMessage : function(message) {
		$.getScript(this.path, function() {
			dhtmlx.message({
				text : message
			});
		});
	}
}

var accordion = {
	changeState : function(currentValue) {
		if ($(currentValue).is('.accordion-section-title')) {
			if ($(currentValue).is('.active')) {
				accordion.close();
			} else {
				accordion.close();
				$(currentValue).addClass('active');
				$('.accordion ' + $(currentValue).attr('name')).slideDown(400)
						.addClass('open');
			}
		}
	},
	close : function() {
		$('.accordion .accordion-section-title').removeClass('active');
		$('.accordion .accordion-section-content').slideUp(400).removeClass(
				'open');
	}
}

var utils = {
	equals : function(x, y) {
		if (x === y) return true;
		if (!(x instanceof Object) || !(y instanceof Object)) return false;
		if (x.constructor !== y.constructor) return false;
		for ( var p in x) {
			if (!x.hasOwnProperty(p)) continue;
			if (!y.hasOwnProperty(p)) return false;
			if (x[p] === y[p]) continue;
			if (typeof (x[p]) !== "object") return false;
			if (!Object.equals(x[p], y[p])) return false;
		}
		for (p in y) {
			if (y.hasOwnProperty(p) && !x.hasOwnProperty(p)) return false;
		}
		return true;
	},
	contains : function(array, object) {		
		var i = array.length;
		while (i--) {
			if (utils.equals(array[i], object)) {
				return true;
			}
		}
		return false;
	}
}

var menuParameters = function(){
	this.newsListView = false;
	this.newsDeleteView = false;
	this.addFormView = false,
	this.editFormView = false,
	this.backView = false,
	this.sortedMenu = false,
	this.editMenu = false;
	this.data = null
}

var template = {
	pagination : 'resources/template/pagination-template.html',
	listNews : 'resources/template/list-news-template.html',
	singleNews : 'resources/template/single-news.html',
	singleComment : 'resources/template/single-comment.html',
	sortedMenu : 'resources/template/menu-template.html',
	editMenu : 'resources/template/menu-template-edit.html',
	newsForm : 'resources/template/news-form.html',

	getListNews : function(data) {
		$("#common-template").empty();
		if (Array.isArray(data)) {
			$.get(this.listNews, function(html) {
				$.each(data, function(index, item) {
					$("#common-template").append(html);
					$('.title:last').text(item.title);
					$('.shortText:last').text(item.shortText);
					$('.modificationDate:last').text(item.modificationDate);
					$('.one-news-view:last').click(function(e) {
						news.getSingleNews.action(item.newsId);
					});
					$('.edit-form:last').click(function(e) {
						news.openEditForm.action(item.newsId);
					});
					$('.delete-news:last').click(function(e) {
						news.deleteNews.action(item.newsId);
					});
				});
				localization.update("#common-template");
			});
		}
	},

	getSingleNews : function(data) {
		$('#common-template').empty();
		if (!Array.isArray(data)) {
			$.get(this.singleNews, function(html) {
				$("#common-template").append(html);
				$('#newsId').val(data.newsId);
				$('#modificationDate').text(data.modificationDate);
				$('#title').text(data.title);
				$('#shortText').text(data.shortText);
				$('#fullText').text(data.fullText);
				
				if (data.tags != undefined) {
					$.each(data.tags, function(idex, item) {
						var p = document.createElement("LABEL");
						var text = document.createTextNode(item.tagName);
						p.setAttribute("class", "tag-text");
						p.appendChild(text);
						$('.news-tags-block').append(p);
					});
				}
				$('#add-comment-form').submit(function(event) {
					var commentText = $('.new-comment-text').val().trim();
					if (validation.isValidComment(commentText)) {
						var requestData = {
							"newsId" : data.newsId,
							"commentText" : commentText
						};
						comment.addComment.action(requestData);
					}
				});
				localization.update("#common-template");
			});
		}
	},

	getEditForm : function(data) {
		$('#common-template').empty();
		$.get(this.newsForm, function(html) {
			$("#common-template").append(html);
			$('#newsId').val(data.newsId);
			$('#title').text(data.title);
			$('#shortText').text(data.shortText);
			$('#fullText').text(data.fullText);
			$('#save-news-form').submit(function(event) {
				var data = news.getDataFromForm();
				if (data !== null) {
					news.editNews.action(data);
				}
			});
			$('#cancel-save-news').click(function(event) {
				news.getListNews.action();
			});
			news.checkForm.setOriginalState(data.title, data.shortText, data.fullText);
			localization.update("#common-template");
		});
	},

	getAddForm : function() {
		$('#common-template').empty();
		$.get(this.newsForm, function(html) {
			$("#common-template").append(html);
			$('#save-news-form').submit(function(event) {
				var data = news.getDataFromForm();
				if (data !== null) {
					news.addNews.action(data);
				}
			});
			$('#cancel-save-news').click(function(event) {
				news.getListNews.action();
			});
			news.checkForm.setOriginalState("", "", "");
			news.checkForm.setTags([]);
			news.checkForm.setAuthors([]);
			localization.update("#common-template");
		});
	},

	getCommentsList : function(data) {
		if (Array.isArray(data)) {
			$('.news-comments-block').empty();
			$.get(this.singleComment, function(html) {
				$.each(data, function(index, item) {
					$('.news-comments-block').append(html);
					$('.comment-creationDate:last').text(item.creationDate);
					$('.comment-text:last').text(item.commentText);
					$('.delete-comment:last').click(function(e) {
						comment.deleteCommentById.action(item.commentId, item.newsId);
						});
					});
				localization.update("#common-template");
			});
		}
	},

	getNewsPagination : function() {
		$.get(this.pagination, function(html) {
			$("#common-template").append(html);
			$('.previous-page').click(function(e) {
				news.filter.setPage(news.filter.getPage() - 1);
				news.getListNews.action();
			});
			$('.next-page').click(function(e) {
				news.filter.setPage(news.filter.getPage() + 1);
				news.getListNews.action();
			});
			$(".current-page").append(news.filter.getPage());
			if (news.filter.getPage() <= 1) {
				$('.previous-btn').prop("disabled", true);				
				$('.previous-page').unbind('click');
			} else {
				$('.previous-btn').prop("disabled", false);
			} 
			if(news.filter.top !== null){
				$('.next-btn').prop("disabled", true);
				$('.next-page').unbind('click');
			} else if ($('.list-news-block').length === news.filter.getSize()) {
				var filter = news.filter.getCurrentState();
				filter.offset = filter.offset + filter.size
				var path = request.url.newsByFilter + $.param(filter);
				request.send(path, null, request.type.GET, function(response) {
					if (response == null || response.length == 0){
						$('.next-btn').prop("disabled", true);
						$('.next-page').unbind('click');
					}						
					else
						$('.next-btn').prop("disabled", false);
				});
			} else if ($('.list-news-block').length < news.filter.getSize()) {
				$('.next-btn').prop("disabled", true);
				$('.next-page').unbind('click');
			} else {
				$('.next-btn').prop("disabled", false);
			}
			localization.update(".pagination");
		});
	},

	getCommentsPagination : function(newsId) {
		$.get(this.pagination, function(html) {
			$(".news-comments-block").append(html);
			$('.previous-page').click(function(e) {
				comment.filter.setPage(comment.filter.getPage() - 1);
				comment.getListCommentsByNewsId.action(newsId);
			});
			$('.next-page').click(function(e) {
				comment.filter.setPage(comment.filter.getPage() + 1);
				comment.getListCommentsByNewsId.action(newsId);
			});
			$(".current-page").append(comment.filter.getPage());
			if (comment.filter.getPage() <= 1) {
				$('.previous-btn').prop("disabled", true);	
				$( ".previous-page" ).unbind('click');
			} else {
				$('.previous-btn').prop("disabled", false);
			}
			if ($('.single-news-comment-block').length < comment.filter.getSize()) {
				$('.next-btn').prop("disabled", true);
				$('.next-page').unbind('click');
			} else if($('.single-news-comment-block').length === comment.filter.getSize()){
				var params = comment.filter.getCurrentState(newsId);
				params.offset = params.offset + params.size
				var path = request.url.commentsByFilter + $.param(params);
				request.send(path, null, request.type.GET, function(response) {
					if (response == null || response.length == 0){
						$('.next-btn').prop("disabled", true);
						$('.next-page').unbind('click');
					} else {
						$('.next-btn').prop("disabled", false);
					}						
				});
			} else {
				$('.next-btn').prop("disabled", false);
			}
			localization.update(".pagination");
		});
	},

	getMenu : function(params) {		
		$('#menu-template').empty();
		$('#back-template').empty();
		if (params.newsListView) {
			var x = document.createElement("INPUT");
			x.setAttribute("type", "button");
			x.setAttribute("data-i18n", "[value]content.btnListView");
			x.setAttribute("class", "link-view");
			x.addEventListener("click", function() {
				if (news.checkForm.isChanged()) {
					message.getConfirm(null, i18n.t("content.warning"), function(answer) {
						if (answer) {
							news.filter.setFilter(1, null, null, null);
							news.getListNews.action();
							}
						});
				} else {
					news.filter.setFilter(1, null, null, null);
					news.getListNews.action();
				}				
			});
			$('#menu-template').append(x);
			$('#menu-template').append('<br>');
		}
		if (params.addFormView) {
			var x = document.createElement("INPUT");
			x.setAttribute("type", "button");
			x.setAttribute("data-i18n", "[value]content.btnAddView");
			x.setAttribute("class", "link-view");
			x.addEventListener("click", function() {
				news.openAddForm.action();
			});
			$('#menu-template').append(x);
			$('#menu-template').append('<br>');
		}
		if (params.data instanceof Object) {
			if (params.newsDeleteView) {
				var x = document.createElement("INPUT");
				x.setAttribute("type", "button");
				x.setAttribute("data-i18n", "[value]content.btnDelete");
				x.setAttribute("class", "link-view");
				x.addEventListener("click", function() {
					news.deleteNews.action(params.data.newsId);
				});
				$('#menu-template').append(x);
				$('#menu-template').append('<br>');
			}
			if (params.editFormView) {
				var x = document.createElement("INPUT");
				x.setAttribute("type", "button");
				x.setAttribute("data-i18n", "[value]content.btnEditView");
				x.setAttribute("class", "link-view");
				x.addEventListener("click", function() {
					news.openEditForm.action(params.data.newsId);
				});
				$('#menu-template').append(x);
				$('#menu-template').append('<br>');
			}			
		}
		if (params.sortedMenu) {
			template.getSortedMenu();
		}
		if (params.editMenu) {
			template.getEditMenu(params.data);
		}
		if (params.backView) {
			var x = document.createElement("INPUT");
			x.setAttribute("type", "button");
			x.setAttribute("data-i18n", "[value]header.btnBack");
			x.setAttribute("class", "link-view");
			x.addEventListener("click", function() {
				if (news.checkForm.isChanged()) {
					message.getConfirm(null, i18n.t("content.warning"), function(answer) {
						if (answer) windowHistory.goBack();
						});
				}else{
					windowHistory.goBack();
				}				
			});
			$('#back-template').append(x);
		}
		localization.update('#menu-template');
		localization.update('#back-template');
	},

	getSortedMenu : function() {
		$.get(this.sortedMenu, function(html) {
			$('#menu-template').append(html);
			tag.getTagsCount();
			author.getAuthorCount();
			$('.comment-top').click(function(e) {
				news.filter.setFilter(1, null, null, $(this).attr("title"));
				news.getListNews.action();
			});
			$('.accordion-section-title').click(function(e) {
				accordion.changeState(this);
			});
			localization.update('.accordion');
		});
	},

	getEditMenu : function(data) {
		$.get(this.editMenu, function(html) {
			$('#menu-template').append(html);
			tag.getTags(function(response) {
				template.loadEditableTags(response);
				if (data instanceof Object && data.tags != undefined) {
					$.each(data.tags, function(index, item) {
						$("#" + item.tagName).prop("checked", true);
					});
					news.checkForm.setTags(tag.getTagsFromForm());
				}
			});
			author.getAuthors(function(response) {
				template.loadEditableAuthors(response);
				if (data instanceof Object && data.authors != undefined) {
					$.each(data.authors, function(index, item) {
						$("#" + item.authorId).prop("checked", true);
					});
					news.checkForm.setAuthors(author.getAuthorsFromForm());
				}
			});					
			$('#add-tag-form').submit(function(event) {
				var tagText = $('.text-tag').val().trim();
				if($('#' + tagText).length > 0){
					message.getMessage(i18n.t("content.tag_violation_equivalence"))
				}else if (validation.isValidTag(tagText)) {
					tag.addTag({"tagName" : tagText});
				}
			});
			$('#add-author-form').submit(function(event) {
				var name = $('.text-author').val().trim();
				if (validation.isValidAuthor(name)) {
					author.addAuthor({"name" : name});
				}
			});
			$('.accordion-section-title').click(function(e) {
				accordion.changeState(this);
			});	
			localization.update('.accordion');
		});
	},

	loadTagsToSortedMenu : function(data) {
		if (Array.isArray(data)) {
			$.each(data, function(key, item) {
				var x = document.createElement("INPUT");
				x.setAttribute("type", "button");
				x.setAttribute("value", item.tagName + ' ('+ item.count + ')');
				x.setAttribute("class", "link-view new-line tag-button");
				x.addEventListener("click", function() {
					news.filter.setFilter(1, item.tagName, null, null);
					news.getListNews.action();
				});
				$('#tags-block').append(x);
				$('#tags-block').append('<br>');
			});
		}
	},

	loadEditableTags : function(data) {
		if (Array.isArray(data)) {
			$('#tags-list').empty();
			$.each(data, function(key, item) {
				template.loadTagToMenu(item);
			});
		}
	},

	loadTagToMenu : function(item) {
		if (!Array.isArray(item)) {
			var label = document.createElement("LABEL");
			label.setAttribute("for", item.tagName);
			var text = document.createTextNode(item.tagName);
			label.appendChild(text);
			var checkbox = document.createElement("INPUT");
			checkbox.setAttribute("type", "checkbox");
			checkbox.setAttribute("name", "tags-checkbox");
			checkbox.setAttribute("class", "tags-checkbox")
			checkbox.setAttribute("id", item.tagName);
			checkbox.setAttribute("value", item.tagName);
			$('#tags-list').append(checkbox);
			$('#tags-list').append(label);			
			$('#tags-list').append('<br>');
		}
	},

	loadAuthorsToSortedMenu : function(data) {
		if (Array.isArray(data)) {
			$.each(data, function(key, item) {
				var x = document.createElement("INPUT");
				x.setAttribute("type", "button");
				x.setAttribute("value", item.name + ' (' + item.count + ')');
				x.setAttribute("class", "link-view new-line author-button");
				x.addEventListener("click", function() {
					news.filter.setFilter(1, null, item.authorId, null);
					news.getListNews.action();
				});
				$('#authors-block').append(x);
				$('#authors-block').append('<br>');
			});
		}
	},

		loadEditableAuthors : function(data) {
		if (Array.isArray(data)) {
			$('#authors-list').empty();
			$.each(data, function(key, item) {
				template.loadAuthorToMenu(item);
			});
		}
	},

	loadAuthorToMenu : function(item) {
		if (!Array.isArray(item)) {
			var label = document.createElement("LABEL");
			label.setAttribute("for", item.authorId);
			var text = document.createTextNode(item.name);
			label.appendChild(text);
			var checkbox = document.createElement("INPUT");
			checkbox.setAttribute("type", "checkbox");
			checkbox.setAttribute("name", "authors-checkbox")
			checkbox.setAttribute("class", "authors-checkbox")
			checkbox.setAttribute("id", item.authorId);
			checkbox.setAttribute("value", item.authorId);
			$('#authors-list').append(checkbox);
			$('#authors-list').append(label);			
			$('#authors-list').append('<br>');
		}
	}
}
