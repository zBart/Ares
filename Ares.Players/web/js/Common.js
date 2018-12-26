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
                listItems.filter('.active').removeClass('active');
                listItems.filter('[data-mode-id="' + msg.Title + '"]').addClass('active');

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
                    listElement.addClass('btn btn-outline-primary btn-block text-left d-flex justify-content-between align-items-center');
                    listElement.text(trigger.Name);
                    listElement.click(function() {
                        $.getJSON("triggerElement?Id=" + $(this).data('element-id'), function(resp) {});
                    });

                    var listElementIcon = $('<i></i>');
                    listElementIcon.addClass('fas');
                    listElementIcon.toggleClass('fa-volume-mute', !trigger.IsActive);
                    listElementIcon.toggleClass('fa-volume-up', trigger.IsActive);

                    var listElementIconWrapper = $('<span></span>');
                    listElementIconWrapper.addClass('badge');
                    listElementIconWrapper.toggleClass('badge-light', !trigger.IsActive);
                    listElementIconWrapper.toggleClass('badge-success', trigger.IsActive);

                    listElementIconWrapper.append(listElementIcon);
                    listElement.append(listElementIconWrapper);
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
                var listItems = $('#modeSelection2 .list-group-item[data-mode-id]');
                $('.badge', listItems).text('');

                // Remove all
                $('.fa-volume-up', elementsContainer)
                    .removeClass('fa-volume-up')
                    .addClass('fa-volume-mute')
                    .parent()
                    .removeClass('badge-success')
                    .addClass('badge-light');

                var modesWithActive = {};

                var activeList = getElementById("activeElements2");
                if (activeList != null) {
                    var activeListText = "";
                    for (var i = 0; i < msg.Triggers.length; ++i) {
                        $('[data-element-id="' + msg.Triggers[i].Id + '"] .fa-volume-mute', elementsContainer)
                            .removeClass('fa-volume-mute')
                            .addClass('fa-volume-up')
                            .parent()
                            .removeClass('badge-light')
                            .addClass('badge-success');

                        activeListText += msg.Triggers[i].Name;
                        if (i < msg.Triggers.length - 1)
                            activeListText += ", ";

                        listItems.filter('[data-elements-' + msg.Triggers[i].Id + ']').each(function() {
                            var modeId = $(this).data('mode-id');

                            if (!modesWithActive[modeId]) {
                                modesWithActive[modeId] = 0;
                            }

                            modesWithActive[modeId]++;
                        });
                    }

                    activeList.innerHTML = activeListText;
                }

                for (var mode in modesWithActive) {
                    $('.badge', listItems.filter('[data-mode-id="' + mode + '"]')).text(modesWithActive[mode]);
                }
            },

            /**
             *
             */
            MusicInfo: function(msg, e) {
                console.log('MusicInfo', msg);

                $('#currentMusic').text(msg.LongTitle);
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

                for (var i = 0; i < msg.Ids.length; ++i) {
                    var newLink = $('<button></button');
                    newLink.addClass('btn btn-link btn-block text-left');
                    newLink.html('<i class="fas fa-play-circle"></i> ' + msg.Titles[i]);
                    newLink.data('id', msg.Ids[i]);
                    newLink.click(function() {
	                    $.getJSON("selectMusicElement?Id=" + $(this).data('id'), function(resp) {});
                    });

                    musicList.append(newLink);
                }
            },

            /**
             *
             */
            MusicRepeatInfo: function(msg, e) {
                var repeatButton = $('#repeatButton');

                if (msg.Repeat) {
                    repeatButton.addClass('active');
                    repeatButton.unbind('click').click(repeatOff);
                } else {
                    repeatButton.removeClass('active');
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

                for (var i = 0; i < msg.Categories.length; ++i) {
                    var optionElement = $('<option></option>');
                    optionElement.prop('selected', (msg.Categories[i].Id === msg.ActiveCategory));
                    optionElement.prop('id', msg.Categories[i].Id);
                    optionElement.text(msg.Categories[i].Name);
                    categorySelect.append(optionElement);
                }

                if (msg.Categories.length > 0) {
                    updateTags(msg.TagsPerCategory[msg.ActiveCategory]);
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

                combineCombo.prop('selectedIndex', msg.CategoryCombination);

                var elementIndicatorList = document.getElementsByClassName("elementTriggerIndicator");
                for (vi = 0; i < elementIndicatorList.length; ++i) {
                    elementIndicatorList[i].src = "/web/image/redlight.png";
                    elementIndicatorList[i].alt = "off";
                }

                var activeList = $('#activeTags');
                var activeListText = '';
                for (var i = 0; i < msg.Tags.length; ++i) {
                    updateTagElementIndicator(msg.Tags[i].Id, msg.Tags[i].IsActive);
                    activeListText += msg.Tags[i].Name;
                    if (i < msg.Tags.length - 1)
                        activeListText += ', ';
                }

                activeList.html(activeListText);
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

function switchToPlaylist() {
	var inlinePlaylist = $('#inlinePlaylist');

    if (!inlinePlaylist.hasClass('d-none')) {
	    return;
    }

    $('#elements').addClass('d-none');
    inlinePlaylist.removeClass('d-none');
    
    $.getJSON("resendInfo?InfoId=MusicList", function(resp) {});
}

function switchToElements() {
	var elements = $('#elements');

	if (!elements.hasClass('d-none')) {
		return;
	}

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

function updateTags(tagList) {
    var elementsSelection = getElementById("elementsSelection");
    if (!elementsSelection) return;
    elementsSelection.innerHTML = "";
    for (i = 0; i < tagList.length; ++i) {
        var trigger = tagList[i];
        var listElement = document.createElement('li');
        listElement.setAttribute("class", "trigger");
        var newTriggerIndicator = document.createElement('img');
        newTriggerIndicator.setAttribute("id", "indicator" + trigger.Id);
        newTriggerIndicator.setAttribute("class", "elementTriggerIndicator");
        newTriggerIndicator.src = trigger.IsActive ? "/web/image/greenlight.png" : "/web/image/redlight.png";
        listElement.appendChild(newTriggerIndicator);
        var newTriggerButton = document.createElement('input');
        newTriggerButton.type = "button";
        newTriggerButton.onclick = function() {
	        $.getJSON("switchTag?Id=" + this.id);
        };
        newTriggerButton.setAttribute("id", trigger.Id);
        newTriggerButton.setAttribute("value", trigger.Name);
        newTriggerButton.setAttribute("class", "elementTriggerButton");
        listElement.appendChild(newTriggerButton);
        elementsSelection.appendChild(listElement);
    }
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
