$(function() {
    /**
     * Event source
     */
    $.source = new EventSource('/event-stream?channel=Control&t=' + new Date().getTime());
    $.source.addEventListener(
        'error',
        function(e) {
            console.log(e);
        },
        false
    );

    $.ss.eventReceivers = { "document": document };

    $($.source).handleServerEvents({
        handlers: {
            ErrorInfo: function(msg, e) {
                toast(msg.ErrorMessage);
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
                            selectMode2($(this).data('mode-id'));
                        });

                        // Badge
                        var buttonBadge = $('<span></span>');
                        buttonBadge.addClass('badge badge-secondary');
                        newModeButton.append(buttonBadge);

                        modeSelection.append(newModeButton);
                    }
                }
            },
            ImportInfo: function(msg, e) {
                // Loaded project
                $('#loaded-project')
                    .html((msg.Percent < 0 || msg.Percent > 100) ? '...' : msg.Percent + ' %')
                    .parents('.nav-item')
                    .removeClass('d-none');
            },
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
                        triggerElement($(this).data('element-id'));
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

                var currentMusic = getElementById("currentMusic");
                if (currentMusic != null) currentMusic.innerHTML = msg.LongTitle;
            },
            MusicListInfo: function(msg, e) {
                console.log('MusicListInfo', msg);

                var musicList = getElementById("playlistList");
                if (!musicList) return;
                musicList.innerHTML = "";
                for (i = 0; i < msg.Ids.length; ++i) {
                    var newLink = document.createElement('a');
                    newLink.setAttribute("class", "musicListLink");
                    newLink.innerHTML = msg.Titles[i];
                    newLink.id = msg.Ids[i];
                    newLink.onclick = function() { selectElement(this.id); }
                    var newItem = document.createElement('li');
                    newItem.className = "playlist";
                    newItem.appendChild(newLink);
                    musicList.appendChild(newItem);
                }
            },
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
        },
        receivers: {
        },
        success: function(selector, msg, json) {
        }
    });
});

$(document).on('pagebeforehide',
    '#control',
    function() {
        source.close();
    });

function changeVolume(volId, volValue) {
    $.getJSON("changeVolume?Id=" + volId + "&Value=" + volValue, function(resp) {});
}

function selectMode2(mode) {
    $.getJSON("selectMode?Title=" + mode, function(resp) {});
    switchToElements();
}

function triggerElement(id) {
    $.getJSON("triggerElement?Id=" + id, function(resp) {});
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

function getStyle(elementId) {
    var styleObj;
    var el = getElementById(elementId);

    if (typeof window.getComputedStyle != "undefined") {
        styleObj = window.getComputedStyle(el, null);
    } else if (el.currentStyle != "undefined") {
        styleObj = el.currentStyle;
    }
    return styleObj;
}

function switchToPlaylist() {
    if (getStyle("inlinePlaylist").display == 'block') {
        return;
    }
    if (getStyle("elements").display == 'block') {
        getElementById("inlinePlaylist").style.display = 'block';
        getElementById("elements").style.display = 'none';
        $.getJSON("resendInfo?InfoId=MusicList", function(resp) {});
    } else {
        $.mobile.pageContainer.pagecontainer("change", "/Playlist", {});
    }
}

function switchToElements() {
    if (getStyle("elements").display == 'block') {
        return;
    }
    if (getStyle("inlinePlaylist").display == 'block') {
        getElementById("inlinePlaylist").style.display = 'none';
        getElementById("elements").style.display = 'block';
        $.getJSON("resendInfo?InfoId=Elements", function(resp) {});
    } else {
        $.mobile.pageContainer.pagecontainer("change", "/Elements", {});
    }
}

function findKeyframesRule(rule) {
    var ss = document.styleSheets;
    for (var i = 0; i < ss.length; ++i)
        if (ss[i].cssRules) {
            for (var j = 0; j < ss[i].cssRules.length; ++j) {
                if (ss[i].cssRules[j].name == rule) {
                    return ss[i].cssRules[j];
                }
            }
        }
    return null;
}

function selectElement(id) {
    $.getJSON("selectMusicElement?Id=" + id, function(resp) {});
}

$(document).on("pagecontainershow",
    function(event, ui) {
        var pageId = $('body').pagecontainer('getActivePage').prop('id');
        if (pageId == "tags") {
            $.getJSON("resendInfo?InfoId=Tags", function(resp) {});
        } else if (pageId == "elements") {
            $.getJSON("resendInfo?InfoId=Elements", function(resp) {});
        } else if (pageId == "modes") {
            $.getJSON("resendInfo?InfoId=Modes", function(resp) {});
        } else if (pageId == "playlist") {
            $.getJSON("resendInfo?InfoId=MusicList", function(resp) {});
        }
    });

function getElementById(id) {
    var element = $('#' + id);
    if (element) {
        var domElement = element[0];
        return domElement;
    } else return null;
}

function toPlaylist() {
    $.mobile.pageContainer.pagecontainer("change", "/Playlist", {});
}

function innerWidth(el) {
    var kids = el.children;
    var w = 0;
    for (var i = 0; i < kids.length; i++)
        w += kids[i].offsetWidth;
    return w;
}

function setColumnWidth(list) {
    var items = list.children;
    var mW = 0;
    for (var i = 0; i < items.length; i++) {
        var w = innerWidth(items[i]);
        if (w > mW)
            mW = w;
    }
    mW += 10;
    list.setAttribute("style",
        "column-width:" +
        mW +
        "px;" +
        "-moz-column-width:" +
        mW +
        "px;" +
        "-webkit-column-width:" +
        mW +
        "px");
}

function getProjects() {
    $.mobile.pageContainer.pagecontainer("change", "/GetProjects", {});
}

function switchImg(elemnt, path) {
    elemnt.src = path;
}

function removeClass(element, aClassName) {
    element.className = element.className.replace(new RegExp('(?:^|\\s)' + aClassName + '(?!\\S)'), '');
}

function addClass(element, aClassName) {
    element.className = element.className + " " + aClassName;
}

var toast = function(msg) {
    $("<div class='ui-loader ui-overlay-shadow ui-body-e ui-corner-all'><h3>" + msg + "</h3></div>")
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
}