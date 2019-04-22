$(function() {
    /**
     * Event source
     */
    $.source = new EventSource('/event-stream?channel=interface&t=' + new Date().getTime());
    $.source.addEventListener(
        'error',
        function(e) {
            console.error(e);
        },
        false
    );

    $.ss.eventReceivers = { 'document': document };
    
    /**
     *
     */
    $($.source).handleServerEvents({
        handlers: {
	        /**
	         *
	         */
            ErrorInfo: function(msg, e) {
                $("<div class='ui-loader ui-overlay-shadow ui-body-e ui-corner-all'><h3>" + msg.ErrorMessage + "</h3></div>")
	                .css({
		                display: "block",
		                opacity: 0.90,
		                position: "fixed",
		                padding: "7px",
		                "text-align": "center",
		                width: "270px",
		                left: ($(window).width() - 284) / 2,
		                top: $(window).height() / 2
	                })
	                .appendTo($.mobile.pageContainer).delay(1500)
	                .fadeOut(400,
		                function() {
			                $(this).remove();
		                });
            },

            /**
             * New project information event
             */
            NewProjectInfo: function(msg, e) {
                console.log('NewProjectInfo', msg);

                // Loaded project
                $('#loaded-project')
                    .text(msg.Name)
                    .parents('.nav-item')
                    .removeClass('d-none');

                //
                var modeSelection = $('#modeSelection2');
                if (modeSelection) {
                    modeSelection.empty();

                    for (var i = 0; i < msg.Modes.length; ++i) {
                        var mode = msg.Modes[i];

                        var newModeButton = $('<button></button>');
                        newModeButton.addClass('list-group-item list-group-item-action d-flex justify-content-between align-items-center');
                        newModeButton.text(mode.Name);
                        newModeButton.attr('data-mode-id', mode.Name);
                        
                        for (var j = 0; j < mode.Elements.length; ++j) {
                            newModeButton.attr('data-elements-' + mode.Elements[j].Id, false);
                        }

                        newModeButton.click(function() {
                            $.getJSON("selectMode?Title=" + $(this).data('mode-id'), function(resp) {});
                            switchToElements();
                        });

                        // Badge
                        var buttonBadge = $('<span></span>');
                        buttonBadge.addClass('badge badge-secondary');
                        newModeButton.append(buttonBadge);

                        modeSelection.append(newModeButton);
                    }
                }
            },

            /**
             *
             */
            ImportInfo: function(msg, e) {
                // Loaded project
                $('#loaded-project')
                    .html((msg.Percent < 0 || msg.Percent > 100) ? '...' : msg.Percent + ' %')
                    .parents('.nav-item')
                    .removeClass('d-none');
            },

            /**
             *
             */
            VolumeInfo: function(msg, e) {
                switch (msg.Id) {
                case 0:
                    setVolumeValue('volume-range-sounds', msg.Value);
                    break;
                case 1:
                    setVolumeValue('volume-range-music', msg.Value);
                    break;
                case 2:
                    setVolumeValue('volume-range-overall', msg.Value);
                    break;
                }
            },

            /**
             * Mode info event
             */
            ModeInfo: function(msg, e) {
                console.log('ModeInfo', msg);

                // Mark mode as active
                var listItems = $('#modeSelection2 .list-group-item');
                listItems.filter('.list-group-item-dark').removeClass('list-group-item-dark');
                listItems.filter('[data-mode-id="' + msg.Title + '"]').addClass('list-group-item-dark');

                //
                var elementsContainer = $('#elements-container');

                if (!elementsContainer) {
                    return;
                }

                elementsContainer.empty();

                for (var i = 0; i < msg.Triggers.length; ++i) {
                    var trigger = msg.Triggers[i];
                    var listElementWrapper = $('<div></div>');
                    listElementWrapper.addClass('col-xl-3 col-md-6 p-1');

                    var listElement = $('<button></button');
                    listElement.attr('data-element-id', trigger.Id);
                    listElement.attr('data-is-active', trigger.IsActive);
                    listElement.addClass('btn btn-block text-left d-flex justify-content-between align-items-center btn-outline-secondary p-1');
                    listElement.click(function() {
                        $.getJSON("triggerElement?Id=" + $(this).data('element-id'), function(resp) {});
                    });

                    var listElementName = $('<span></span>');
                    listElementName.addClass('px-2 py-1');
                    listElement.append(listElementName);

                    if (trigger.IsMacro) {
	                    listElementName.append($('<i></i>').addClass('fas fa-cogs'));
	                    listElementName.append(' ');
                    } else {
						var listElementIcon = $('<i></i>');
	                    listElementIcon.addClass('fas');

	                    if (trigger.IsMusic) {
		                    listElementName.append($('<i></i>').addClass('fas fa-music'));
		                    listElementName.append(' ');

		                    listElementIcon.toggleClass('fa-volume-mute', !trigger.IsActive);
		                    listElementIcon.toggleClass('fa-volume-up', trigger.IsActive);
	                    }
                        
	                    listElementIcon.toggleClass('fa-volume-mute', !trigger.IsActive);
	                    listElementIcon.toggleClass('fa-volume-up', trigger.IsActive);

	                    var listElementIconWrapper = $('<span></span>');
	                    listElementIconWrapper.addClass('text-sm px-2 py-1 rounded');
	                    listElementIconWrapper.toggleClass('text-light', trigger.IsActive);
	                    listElementIconWrapper.toggleClass('bg-success', trigger.IsActive);

	                    listElementIconWrapper.append(listElementIcon);
	                    listElement.append(listElementIconWrapper);
                    }

                    listElementName.append(trigger.Name);
                    listElementWrapper.append(listElement);
                    elementsContainer.append(listElementWrapper);
                }
            },

            /**
             * Event that contains all active items
             */
            ActiveElementsInfo: function(msg, e) {
                console.log('ActiveElementsInfo', msg);
                
                var elementsContainer = $('#elements-container');

                // Clear the active number of elements in the modes list
                var listItems = $('#modeSelection2 .list-group-item[data-mode-id]');
                $('.badge', listItems).text('');

                // Reset the playing / not playing element indicators
                var activeElements = $('[data-is-active="true"]', elementsContainer);
                activeElements.attr('data-is-active', false);

                $('.fa-volume-up', activeElements)
                    .removeClass('fa-volume-up')
                    .addClass('fa-volume-mute')
                    .parent()
                    .removeClass('text-light')
                    .removeClass('bg-success');

                var modesWithActive = {};

                var activeList = $('#activeElements2');
                if (activeList != null) {
	                activeList.empty();

                    if (msg.Triggers.length === 0) {
	                    activeList.append($('<span></span>').addClass('text-muted').text('No active elements'));
                    } else {
	                    for (var i = 0; i < msg.Triggers.length; ++i) {
		                    // Mark element as active
		                    var targetElement = $('[data-element-id="' + msg.Triggers[i].Id + '"]', elementsContainer);
		                    targetElement.attr('data-is-active', true);

		                    $('.fa-volume-mute', targetElement)
			                    .removeClass('fa-volume-mute')
			                    .addClass('fa-volume-up')
			                    .parent()
			                    .addClass('text-light')
			                    .addClass('bg-success');

		                    listItems.filter('[data-elements-' + msg.Triggers[i].Id + ']').each(function() {
			                    var modeId = $(this).data('mode-id');

			                    if (!modesWithActive[modeId]) {
				                    modesWithActive[modeId] = 0;
			                    }

			                    modesWithActive[modeId]++;
		                    });
                        
		                    // Update the list
		                    var activeElement = $('<button></button>');
		                    activeElement.attr('data-element-id', msg.Triggers[i].Id);
		                    activeElement.addClass('btn btn-outline-danger btn-sm m-1');

		                    activeElement.click(function() {
			                    $.getJSON("triggerElement?Id=" + $(this).data('element-id'), function(resp) {});
		                    });

		                    if (msg.Triggers[i].IsMusic) {
			                    activeElement
				                    .append($('<i></i>').addClass('fas fa-music'))
				                    .append(' ');
		                    }

		                    activeElement.append(msg.Triggers[i].Name);

		                    activeList.append(activeElement);
	                    }
                    }
                }
                
                
                $('#active-elements-count')
	                .text(msg.Triggers.length)
					.toggleClass('d-none', msg.Triggers.length === 0);

                listItems.each(function() {
	                var self = $(this);
	                var activeCount = self.data('mode-id') in modesWithActive
		                ? modesWithActive[self.data('mode-id')]
		                : 0; 

                    $('.badge', self).text(activeCount > 0 ? activeCount : '');
                });
            },

            /**
             *
             */
            MusicInfo: function(msg, e) {
                console.log('MusicInfo', msg);

                var currentMusic = $('#currentMusic');
                currentMusic.empty();

                if (msg.LongTitle.length === 0) {
	                currentMusic.append($('<span></span>').addClass('text-muted').text('No active music'));
                } else {
	                currentMusic.text(msg.LongTitle);
                }
            },

            /**
             *
             */
            MusicListInfo: function(msg, e) {
                console.log('MusicListInfo', msg);

                var musicList = $('#playlistList');

                if (!musicList) {
	                return;
                }

                musicList.empty();
                
                if (msg.Ids.length === 0) {
	                musicList.append($('<span></span>').addClass('text-muted').text('No active music'));
                } else {
	                for (var i = 0; i < msg.Ids.length; ++i) {
		                var newLink = $('<button></button');
		                newLink.addClass('btn btn-link btn-block text-left');
		                newLink.html('<i class="fas fa-play-circle"></i> ' + msg.Titles[i]);
		                newLink.data('id', msg.Ids[i]);
		                newLink.click(function() {
			                $.getJSON('selectMusicElement?Id=' + $(this).data('id'), function(resp) {});
		                });

		                musicList.append(newLink);
	                }
                }
            },

            /**
             *
             */
            MusicRepeatInfo: function(msg, e) {
                var repeatButton = $('#repeatButton');

                if (msg.Repeat) {
                    repeatButton.addClass('active').find('.fa-slash').addClass('d-none');
                    repeatButton.unbind('click').click(repeatOff);
                } else {
                    repeatButton.removeClass('active').find('.fa-slash').removeClass('d-none');
                    repeatButton.unbind('click').click(repeatOn);
                }
            },
            
            /**
             *
             */
            TagInfo: function (msg, e) {
	            console.log('TagInfo', msg);

                var categorySelect = $('#categorySelect');
				categorySelect.empty();

                var tagsContainer = $('#tagsContainer');
                
				if (!tagsContainer) {
	                return;
                }

				tagsContainer.empty();
                
				for (var i = 0; i < msg.Categories.length; ++i) {
					tagsContainer.append($('<h4></h4>').text(msg.Categories[i].Name));

					var tagContainer = $('<div></div>');
					tagContainer.addClass('row mb-3 mx-n1');

                    for (var j = 0; j < msg.TagsPerCategory[msg.Categories[i].Id].length; ++j) {
	                    var trigger = msg.TagsPerCategory[msg.Categories[i].Id][j];
						var listElementWrapper = $('<div></div>');
	                    listElementWrapper.addClass('col-xl-3 col-md-6 p-1');

	                    var listElement = $('<button></button');
	                    listElement.attr('data-tag-id', trigger.Id);
	                    listElement.attr('data-is-active', trigger.IsActive);
	                    listElement.addClass('btn btn-block text-left d-flex justify-content-between align-items-center btn-outline-secondary p-1');
	                    listElement.click(function() {
	                        $.getJSON("switchTag?Id=" + $(this).data('tag-id'), function(resp) {});
	                    });

	                    var listElementName = $('<span></span>');
	                    listElementName.addClass('px-2 py-1');
	                    listElement.append(listElementName);

	                    if (trigger.IsMacro) {
		                    listElementName.append($('<i></i>').addClass('fas fa-cogs'));
		                    listElementName.append(' ');
	                    } else {
							var listElementIcon = $('<i></i>');
		                    listElementIcon.addClass('fas');

		                    if (trigger.IsMusic) {
			                    listElementName.append($('<i></i>').addClass('fas fa-music'));
			                    listElementName.append(' ');

			                    listElementIcon.toggleClass('fa-volume-mute', !trigger.IsActive);
			                    listElementIcon.toggleClass('fa-volume-up', trigger.IsActive);
		                    }
	                        
		                    listElementIcon.toggleClass('fa-volume-mute', !trigger.IsActive);
		                    listElementIcon.toggleClass('fa-volume-up', trigger.IsActive);

		                    var listElementIconWrapper = $('<span></span>');
		                    listElementIconWrapper.addClass('text-sm px-2 py-1 rounded');
		                    listElementIconWrapper.toggleClass('text-light', trigger.IsActive);
		                    listElementIconWrapper.toggleClass('bg-success', trigger.IsActive);

		                    listElementIconWrapper.append(listElementIcon);
		                    listElement.append(listElementIconWrapper);
	                    }

	                    listElementName.append(trigger.Name);
	                    listElementWrapper.append(listElement);
	                    tagContainer.append(listElementWrapper);
	                }

	                tagsContainer.append(tagContainer);
                }

            },
            
            /**
             *
             */
            ActiveTagInfo: function (msg, e) {
	            console.log('ActiveTagInfo', msg);

                var fadeTimeInput = $('#fadeTimeInput');
                var combineCombo = $('#combineSelect');

                if (!fadeTimeInput || !combineCombo) {
	                return;
                }

                fadeTimeInput.val(msg.FadeTime);
                
                var tagsContainer = $('#tagsContainer');
                
                // Reset the playing / not playing element indicators
                var activeElements = $('[data-is-active="true"]', tagsContainer);
                activeElements.attr('data-is-active', false);

                $('.fa-volume-up', activeElements)
                    .removeClass('fa-volume-up')
                    .addClass('fa-volume-mute')
                    .parent()
                    .removeClass('text-light')
                    .removeClass('bg-success');
                    
				for (var i = 0; i < msg.Tags.length; ++i) {
                    // Mark element as active
                    var targetTag = $('[data-tag-id="' + msg.Tags[i].Id + '"]', tagsContainer);
                    targetTag.attr('data-is-active', true);

                    $('.fa-volume-mute', targetTag)
	                    .removeClass('fa-volume-mute')
	                    .addClass('fa-volume-up')
	                    .parent()
	                    .addClass('text-light')
	                    .addClass('bg-success');
                }
            },
            
            /**
             *
             */
            TagFadingInfo: function (msg, e) {
	            console.log('TagFadingInfo', msg);

                var fadeTimeInput = getElementById('fadeTimeInput');
                if (!fadeTimeInput) return;
                fadeTimeInput.value = msg.FadeTime;
                var fadeBox = getElementById('fadeOnChangeBox');
                if (!fadeBox) return;
                fadeBox.checked = msg.FadeOnlyOnChange;
            }
        },
        receivers: {
        },
        success: function(selector, msg, json) {
        }
    });
});

function changeVolume(volId, volValue) {
    $.getJSON('changeVolume?Id=' + volId + "&Value=" + volValue, function(resp) {});
}

function setVolumeValue(elementId, value) {
    var element = getElementById(elementId);
    if (element) {
        element.value = value;
    }
}

function stopAll() {
    $.getJSON("mainControl?Command=Stop", function(resp) {});
}

function back() {
    $.getJSON("mainControl?Command=Back", function(resp) {});
}

function forward() {
    $.getJSON("mainControl?Command=Forward", function(resp) {});
}

function repeatOn() {
    $.getJSON("mainControl?Command=RepeatOn", function(resp) {});
}

function repeatOff() {
    $.getJSON("mainControl?Command=RepeatOff", function(resp) {});
}

function stopActiveMode() {
	$('#elements-container button[data-is-active="true"]').click();
}

function startActiveMode() {
	$('#elements-container button[data-is-active="false"]').click();
}

function switchToTags() {
	var tags = $('#tags');

	if (!tags.hasClass('d-none')) {
		return;
	}

	$('#modeSelection2 .list-group-item.list-group-item-dark').removeClass('list-group-item-dark');

	$('#elements').addClass('d-none');
	$('#inlinePlaylist').addClass('d-none');
	tags.removeClass('d-none');
    
	$.getJSON("resendInfo?InfoId=TagInfo", function(resp) {});
}

function switchToPlaylist() {
	var inlinePlaylist = $('#inlinePlaylist');

	if (!inlinePlaylist.hasClass('d-none')) {
		return;
	}

	$('#modeSelection2 .list-group-item.list-group-item-dark').removeClass('list-group-item-dark');
    
	$('#tags').addClass('d-none');
	$('#elements').addClass('d-none');
	inlinePlaylist.removeClass('d-none');
    
	$.getJSON("resendInfo?InfoId=MusicList", function(resp) {});
}

function switchToElements() {
	var elements = $('#elements');

	if (!elements.hasClass('d-none')) {
		return;
	}
    
	$('#tags').addClass('d-none');
	$('#inlinePlaylist').addClass('d-none');
	elements.removeClass('d-none');

	$.getJSON("resendInfo?InfoId=Elements", function(resp) {});
}

function getElementById(id) {
    var element = $('#' + id);
    if (element) {
        var domElement = element[0];
        return domElement;
    } else return null;
}

function cancel_Settings() {
	$.mobile.back();
}

function submitData_Settings(sourcePage) {
	var lang = getElementById("languageDe").checked ? "de" : "en";
	var fadeTime = getElementById("fadeTimeInput").value;
	var fadeOption = 0;
	if (getElementById("manualFadingOutIn").checked) fadeOption = 1;
	else if (getElementById("manualFadingCross").checked) fadeOption = 2;
	var onAllSpeakers = getElementById("allSpeakers").checked;
	$.getJSON("changeSettings?PlayMusicOnAllSpeakers=" + onAllSpeakers + "&FadingOption=" + fadeOption + "&FadingTime=" + fadeTime + "&Language=" + lang);
	$.mobile.pageContainer.pagecontainer("change", "/" + sourcePage, { reload: 'true' });
}

function updateTagElementIndicator(id, isActive) {
    var indicator = getElementById("indicator" + id);
    if (indicator) {
        indicator.src = isActive ? "/web/image/greenlight.png" : "/web/image/redlight.png";
        indicator.alt = isActive ? "on" : "off";
    }
}

function SelectCategory() {
    var categorySelect = getElementById('categorySelect');
    var selectedId = categorySelect.options[categorySelect.selectedIndex].id;
    $.getJSON("selectTagCategory?Id=" + selectedId, function (resp) { });
}

function ChangeTagCombine() {
    var combineSelect = getElementById('combineSelect');
    var selectedOption = combineSelect.selectedIndex;
    $.getJSON("selectTagCombination?Option=" + selectedOption, function (resp) { });
}

function SetFading() {
    var timeInput = getElementById('fadeTimeInput');
    var fadeOnChangeBox = getElementById('fadeOnChangeBox');
    $.getJSON('setTagFading?Time=' + timeInput.value + '&OnlyOnChange=' + fadeOnChangeBox.checked);
}

function RemoveAllTags() {
    $.getJSON('removeAllTags');
}

function openProject_Projects(fileName)
{
	$.getJSON('openProject?ProjectFileName=' + fileName, function (resp) { });
	$.mobile.pageContainer.pagecontainer("change", "/Control", {});
}
