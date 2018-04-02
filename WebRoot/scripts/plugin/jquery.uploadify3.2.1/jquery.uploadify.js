/*
Uploadify v3.2
Copyright (c) 2012 Reactive Apps, Ronnie Garcia
Released under the MIT License <http://www.opensource.org/licenses/mit-license.php>

SWFUpload: http://www.swfupload.org, http://swfupload.googlecode.com
mmSWFUpload 1.0: Flash upload dialog - http://profandesign.se/swfupload/,  http://www.vinterwebb.se/
SWFUpload is (c) 2006-2007 Lars Huring, Olov Nilzén and Mammon Media and is released under the MIT License:
http://www.opensource.org/licenses/mit-license.php
SWFUpload 2 is (c) 2007-2008 Jake Roberts and is released under the MIT License:
http://www.opensource.org/licenses/mit-license.php

SWFObject v2.2 <http://code.google.com/p/swfobject/> 
is released under the MIT License <http://www.opensource.org/licenses/mit-license.php>
*/
;
var swfobject = function () {
    var aq = "undefined",
    aD = "object",
    ab = "Shockwave Flash",
    X = "ShockwaveFlash.ShockwaveFlash",
    aE = "application/x-shockwave-flash",
    ac = "SWFObjectExprInst",
    ax = "onreadystatechange",
    af = window,
    aL = document,
    aB = navigator,
    aa = false,
    Z = [aN],
    aG = [],
    ag = [],
    al = [],
    aJ,
    ad,
    ap,
    at,
    ak = false,
    aU = false,
    aH,
    an,
    aI = true,
    ah = function () {
        var a = typeof aL.getElementById != aq && typeof aL.getElementsByTagName != aq && typeof aL.createElement != aq,
        e = aB.userAgent.toLowerCase(),
        c = aB.platform.toLowerCase(),
        h = c ? /win/.test(c) : /win/.test(e),
        j = c ? /mac/.test(c) : /mac/.test(e),
        g = /webkit/.test(e) ? parseFloat(e.replace(/^.*webkit\/(\d+(\.\d+)?).*$/, "$1")) : false,
        d = ! +"\v1",
        f = [0, 0, 0],
        k = null;
        if (typeof aB.plugins != aq && typeof aB.plugins[ab] == aD) {
            k = aB.plugins[ab].description;
            if (k && !(typeof aB.mimeTypes != aq && aB.mimeTypes[aE] && !aB.mimeTypes[aE].enabledPlugin)) {
                aa = true;
                d = false;
                k = k.replace(/^.*\s+(\S+\s+\S+$)/, "$1");
                f[0] = parseInt(k.replace(/^(.*)\..*$/, "$1"), 10);
                f[1] = parseInt(k.replace(/^.*\.(.*)\s.*$/, "$1"), 10);
                f[2] = /[a-zA-Z]/.test(k) ? parseInt(k.replace(/^.*[a-zA-Z]+(.*)$/, "$1"), 10) : 0;
            }
        } else {
            if (typeof af.ActiveXObject != aq) {
                try {
                    var i = new ActiveXObject(X);
                    if (i) {
                        k = i.GetVariable("$version");
                        if (k) {
                            d = true;
                            k = k.split(" ")[1].split(",");
                            f = [parseInt(k[0], 10), parseInt(k[1], 10), parseInt(k[2], 10)];
                        }
                    }
                } catch (b) { }
            }
        }
        return {
            w3: a,
            pv: f,
            wk: g,
            ie: d,
            win: h,
            mac: j
        };
    } (),
    aK = function () {
        if (!ah.w3) {
            return;
        }
        if ((typeof aL.readyState != aq && aL.readyState == "complete") || (typeof aL.readyState == aq && (aL.getElementsByTagName("body")[0] || aL.body))) {
            aP();
        }
        if (!ak) {
            if (typeof aL.addEventListener != aq) {
                aL.addEventListener("DOMContentLoaded", aP, false);
            }
            if (ah.ie && ah.win) {
                aL.attachEvent(ax,
                function () {
                    if (aL.readyState == "complete") {
                        aL.detachEvent(ax, arguments.callee);
                        aP();
                    }
                });
                if (af == top) {
                    (function () {
                        if (ak) {
                            return;
                        }
                        try {
                            aL.documentElement.doScroll("left");
                        } catch (a) {
                            setTimeout(arguments.callee, 0);
                            return;
                        }
                        aP();
                    })();
                }
            }
            if (ah.wk) {
                (function () {
                    if (ak) {
                        return;
                    }
                    if (!/loaded|complete/.test(aL.readyState)) {
                        setTimeout(arguments.callee, 0);
                        return;
                    }
                    aP();
                })();
            }
            aC(aP);
        }
    } ();
    function aP() {
        if (ak) {
            return;
        }
        try {
            var b = aL.getElementsByTagName("body")[0].appendChild(ar("span"));
            b.parentNode.removeChild(b);
        } catch (a) {
            return;
        }
        ak = true;
        var d = Z.length;
        for (var c = 0; c < d; c++) {
            Z[c]();
        }
    }
    function aj(a) {
        if (ak) {
            a();
        } else {
            Z[Z.length] = a;
        }
    }
    function aC(a) {
        if (typeof af.addEventListener != aq) {
            af.addEventListener("load", a, false);
        } else {
            if (typeof aL.addEventListener != aq) {
                aL.addEventListener("load", a, false);
            } else {
                if (typeof af.attachEvent != aq) {
                    aM(af, "onload", a);
                } else {
                    if (typeof af.onload == "function") {
                        var b = af.onload;
                        af.onload = function () {
                            b();
                            a();
                        };
                    } else {
                        af.onload = a;
                    }
                }
            }
        }
    }
    function aN() {
        if (aa) {
            Y();
        } else {
            am();
        }
    }
    function Y() {
        var d = aL.getElementsByTagName("body")[0];
        var b = ar(aD);
        b.setAttribute("type", aE);
        var a = d.appendChild(b);
        if (a) {
            var c = 0; (function () {
                if (typeof a.GetVariable != aq) {
                    var e = a.GetVariable("$version");
                    if (e) {
                        e = e.split(" ")[1].split(",");
                        ah.pv = [parseInt(e[0], 10), parseInt(e[1], 10), parseInt(e[2], 10)];
                    }
                } else {
                    if (c < 10) {
                        c++;
                        setTimeout(arguments.callee, 10);
                        return;
                    }
                }
                d.removeChild(b);
                a = null;
                am();
            })();
        } else {
            am();
        }
    }
    function am() {
        var g = aG.length;
        if (g > 0) {
            for (var h = 0; h < g; h++) {
                var c = aG[h].id;
                var l = aG[h].callbackFn;
                var a = {
                    success: false,
                    id: c
                };
                if (ah.pv[0] > 0) {
                    var i = aS(c);
                    if (i) {
                        if (ao(aG[h].swfVersion) && !(ah.wk && ah.wk < 312)) {
                            ay(c, true);
                            if (l) {
                                a.success = true;
                                a.ref = av(c);
                                l(a);
                            }
                        } else {
                            if (aG[h].expressInstall && au()) {
                                var e = {};
                                e.data = aG[h].expressInstall;
                                e.width = i.getAttribute("width") || "0";
                                e.height = i.getAttribute("height") || "0";
                                if (i.getAttribute("class")) {
                                    e.styleclass = i.getAttribute("class");
                                }
                                if (i.getAttribute("align")) {
                                    e.align = i.getAttribute("align");
                                }
                                var f = {};
                                var d = i.getElementsByTagName("param");
                                var k = d.length;
                                for (var j = 0; j < k; j++) {
                                    if (d[j].getAttribute("name").toLowerCase() != "movie") {
                                        f[d[j].getAttribute("name")] = d[j].getAttribute("value");
                                    }
                                }
                                ae(e, f, c, l);
                            } else {
                                aF(i);
                                if (l) {
                                    l(a);
                                }
                            }
                        }
                    }
                } else {
                    ay(c, true);
                    if (l) {
                        var b = av(c);
                        if (b && typeof b.SetVariable != aq) {
                            a.success = true;
                            a.ref = b;
                        }
                        l(a);
                    }
                }
            }
        }
    }
    function av(b) {
        var d = null;
        var c = aS(b);
        if (c && c.nodeName == "OBJECT") {
            if (typeof c.SetVariable != aq) {
                d = c;
            } else {
                var a = c.getElementsByTagName(aD)[0];
                if (a) {
                    d = a;
                }
            }
        }
        return d;
    }
    function au() {
        return !aU && ao("6.0.65") && (ah.win || ah.mac) && !(ah.wk && ah.wk < 312);
    }
    function ae(f, d, h, e) {
        aU = true;
        ap = e || null;
        at = {
            success: false,
            id: h
        };
        var a = aS(h);
        if (a) {
            if (a.nodeName == "OBJECT") {
                aJ = aO(a);
                ad = null;
            } else {
                aJ = a;
                ad = h;
            }
            f.id = ac;
            if (typeof f.width == aq || (!/%$/.test(f.width) && parseInt(f.width, 10) < 310)) {
                f.width = "310";
            }
            if (typeof f.height == aq || (!/%$/.test(f.height) && parseInt(f.height, 10) < 137)) {
                f.height = "137";
            }
            aL.title = aL.title.slice(0, 47) + " - Flash Player Installation";
            var b = ah.ie && ah.win ? "ActiveX" : "PlugIn",
            c = "MMredirectURL=" + af.location.toString().replace(/&/g, "%26") + "&MMplayerType=" + b + "&MMdoctitle=" + aL.title;
            if (typeof d.flashvars != aq) {
                d.flashvars += "&" + c;
            } else {
                d.flashvars = c;
            }
            if (ah.ie && ah.win && a.readyState != 4) {
                var g = ar("div");
                h += "SWFObjectNew";
                g.setAttribute("id", h);
                a.parentNode.insertBefore(g, a);
                a.style.display = "none"; (function () {
                    if (a.readyState == 4) {
                        a.parentNode.removeChild(a);
                    } else {
                        setTimeout(arguments.callee, 10);
                    }
                })();
            }
            aA(f, d, h);
        }
    }
    function aF(a) {
        if (ah.ie && ah.win && a.readyState != 4) {
            var b = ar("div");
            a.parentNode.insertBefore(b, a);
            b.parentNode.replaceChild(aO(a), b);
            a.style.display = "none"; (function () {
                if (a.readyState == 4) {
                    a.parentNode.removeChild(a);
                } else {
                    setTimeout(arguments.callee, 10);
                }
            })();
        } else {
            a.parentNode.replaceChild(aO(a), a);
        }
    }
    function aO(b) {
        var d = ar("div");
        if (ah.win && ah.ie) {
            d.innerHTML = b.innerHTML;
        } else {
            var e = b.getElementsByTagName(aD)[0];
            if (e) {
                var a = e.childNodes;
                if (a) {
                    var f = a.length;
                    for (var c = 0; c < f; c++) {
                        if (!(a[c].nodeType == 1 && a[c].nodeName == "PARAM") && !(a[c].nodeType == 8)) {
                            d.appendChild(a[c].cloneNode(true));
                        }
                    }
                }
            }
        }
        return d;
    }
    function aA(e, g, c) {
        var d, a = aS(c);
        if (ah.wk && ah.wk < 312) {
            return d;
        }
        if (a) {
            if (typeof e.id == aq) {
                e.id = c;
            }
            if (ah.ie && ah.win) {
                var f = "";
                for (var i in e) {
                    if (e[i] != Object.prototype[i]) {
                        if (i.toLowerCase() == "data") {
                            g.movie = e[i];
                        } else {
                            if (i.toLowerCase() == "styleclass") {
                                f += ' class="' + e[i] + '"';
                            } else {
                                if (i.toLowerCase() != "classid") {
                                    f += " " + i + '="' + e[i] + '"';
                                }
                            }
                        }
                    }
                }
                var h = "";
                for (var j in g) {
                    if (g[j] != Object.prototype[j]) {
                        h += '<param name="' + j + '" value="' + g[j] + '" />';
                    }
                }
                a.outerHTML = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"' + f + ">" + h + "</object>";
                ag[ag.length] = e.id;
                d = aS(e.id);
            } else {
                var b = ar(aD);
                b.setAttribute("type", aE);
                for (var k in e) {
                    if (e[k] != Object.prototype[k]) {
                        if (k.toLowerCase() == "styleclass") {
                            b.setAttribute("class", e[k]);
                        } else {
                            if (k.toLowerCase() != "classid") {
                                b.setAttribute(k, e[k]);
                            }
                        }
                    }
                }
                for (var l in g) {
                    if (g[l] != Object.prototype[l] && l.toLowerCase() != "movie") {
                        aQ(b, l, g[l]);
                    }
                }
                a.parentNode.replaceChild(b, a);
                d = b;
            }
        }
        return d;
    }
    function aQ(b, d, c) {
        var a = ar("param");
        a.setAttribute("name", d);
        a.setAttribute("value", c);
        b.appendChild(a);
    }
    function aw(a) {
        var b = aS(a);
        if (b && b.nodeName == "OBJECT") {
            if (ah.ie && ah.win) {
                b.style.display = "none"; (function () {
                    if (b.readyState == 4) {
                        aT(a);
                    } else {
                        setTimeout(arguments.callee, 10);
                    }
                })();
            } else {
                b.parentNode.removeChild(b);
            }
        }
    }
    function aT(a) {
        var b = aS(a);
        if (b) {
            for (var c in b) {
                if (typeof b[c] == "function") {
                    b[c] = null;
                }
            }
            b.parentNode.removeChild(b);
        }
    }
    function aS(a) {
        var c = null;
        try {
            c = aL.getElementById(a);
        } catch (b) { }
        return c;
    }
    function ar(a) {
        return aL.createElement(a);
    }
    function aM(a, c, b) {
        a.attachEvent(c, b);
        al[al.length] = [a, c, b];
    }
    function ao(a) {
        var b = ah.pv,
        c = a.split(".");
        c[0] = parseInt(c[0], 10);
        c[1] = parseInt(c[1], 10) || 0;
        c[2] = parseInt(c[2], 10) || 0;
        return (b[0] > c[0] || (b[0] == c[0] && b[1] > c[1]) || (b[0] == c[0] && b[1] == c[1] && b[2] >= c[2])) ? true : false;
    }
    function az(b, f, a, c) {
        if (ah.ie && ah.mac) {
            return;
        }
        var e = aL.getElementsByTagName("head")[0];
        if (!e) {
            return;
        }
        var g = (a && typeof a == "string") ? a : "screen";
        if (c) {
            aH = null;
            an = null;
        }
        if (!aH || an != g) {
            var d = ar("style");
            d.setAttribute("type", "text/css");
            d.setAttribute("media", g);
            aH = e.appendChild(d);
            if (ah.ie && ah.win && typeof aL.styleSheets != aq && aL.styleSheets.length > 0) {
                aH = aL.styleSheets[aL.styleSheets.length - 1];
            }
            an = g;
        }
        if (ah.ie && ah.win) {
            if (aH && typeof aH.addRule == aD) {
                aH.addRule(b, f);
            }
        } else {
            if (aH && typeof aL.createTextNode != aq) {
                aH.appendChild(aL.createTextNode(b + " {" + f + "}"));
            }
        }
    }
    function ay(a, c) {
        if (!aI) {
            return;
        }
        var b = c ? "visible" : "hidden";
        if (ak && aS(a)) {
            aS(a).style.visibility = b;
        } else {
            az("#" + a, "visibility:" + b);
        }
    }
    function ai(b) {
        var a = /[\\\"<>\.;]/;
        var c = a.exec(b) != null;
        return c && typeof encodeURIComponent != aq ? encodeURIComponent(b) : b;
    }
    var aR = function () {
        if (ah.ie && ah.win) {
            window.attachEvent("onunload",
            function () {
                var a = al.length;
                for (var b = 0; b < a; b++) {
                    al[b][0].detachEvent(al[b][1], al[b][2]);
                }
                var d = ag.length;
                for (var c = 0; c < d; c++) {
                    aw(ag[c]);
                }
                for (var e in ah) {
                    ah[e] = null;
                }
                ah = null;
                for (var f in swfobject) {
                    swfobject[f] = null;
                }
                swfobject = null;
            });
        }
    } ();
    return {
        registerObject: function (a, e, c, b) {
            if (ah.w3 && a && e) {
                var d = {};
                d.id = a;
                d.swfVersion = e;
                d.expressInstall = c;
                d.callbackFn = b;
                aG[aG.length] = d;
                ay(a, false);
            } else {
                if (b) {
                    b({
                        success: false,
                        id: a
                    });
                }
            }
        },
        getObjectById: function (a) {
            if (ah.w3) {
                return av(a);
            }
        },
        embedSWF: function (k, e, h, f, c, a, b, i, g, j) {
            var d = {
                success: false,
                id: e
            };
            if (ah.w3 && !(ah.wk && ah.wk < 312) && k && e && h && f && c) {
                ay(e, false);
                aj(function () {
                    h += "";
                    f += "";
                    var q = {};
                    if (g && typeof g === aD) {
                        for (var o in g) {
                            q[o] = g[o];
                        }
                    }
                    q.data = k;
                    q.width = h;
                    q.height = f;
                    var n = {};
                    if (i && typeof i === aD) {
                        for (var p in i) {
                            n[p] = i[p];
                        }
                    }
                    if (b && typeof b === aD) {
                        for (var l in b) {
                            if (typeof n.flashvars != aq) {
                                n.flashvars += "&" + l + "=" + b[l];
                            } else {
                                n.flashvars = l + "=" + b[l];
                            }
                        }
                    }
                    if (ao(c)) {
                        var m = aA(q, n, e);
                        if (q.id == e) {
                            ay(e, true);
                        }
                        d.success = true;
                        d.ref = m;
                    } else {
                        if (a && au()) {
                            q.data = a;
                            ae(q, n, e, j);
                            return;
                        } else {
                            ay(e, true);
                        }
                    }
                    if (j) {
                        j(d);
                    }
                });
            } else {
                if (j) {
                    j(d);
                }
            }
        },
        switchOffAutoHideShow: function () {
            aI = false;
        },
        ua: ah,
        getFlashPlayerVersion: function () {
            return {
                major: ah.pv[0],
                minor: ah.pv[1],
                release: ah.pv[2]
            };
        },
        hasFlashPlayerVersion: ao,
        createSWF: function (a, b, c) {
            if (ah.w3) {
                return aA(a, b, c);
            } else {
                return undefined;
            }
        },
        showExpressInstall: function (b, a, d, c) {
            if (ah.w3 && au()) {
                ae(b, a, d, c);
            }
        },
        removeSWF: function (a) {
            if (ah.w3) {
                aw(a);
            }
        },
        createCSS: function (b, a, c, d) {
            if (ah.w3) {
                az(b, a, c, d);
            }
        },
        addDomLoadEvent: aj,
        addLoadEvent: aC,
        getQueryParamValue: function (b) {
            var a = aL.location.search || aL.location.hash;
            if (a) {
                if (/\?/.test(a)) {
                    a = a.split("?")[1];
                }
                if (b == null) {
                    return ai(a);
                }
                var c = a.split("&");
                for (var d = 0; d < c.length; d++) {
                    if (c[d].substring(0, c[d].indexOf("=")) == b) {
                        return ai(c[d].substring((c[d].indexOf("=") + 1)));
                    }
                }
            }
            return "";
        },
        expressInstallCallback: function () {
            if (aU) {
                var a = aS(ac);
                if (a && aJ) {
                    a.parentNode.replaceChild(aJ, a);
                    if (ad) {
                        ay(ad, true);
                        if (ah.ie && ah.win) {
                            aJ.style.display = "block";
                        }
                    }
                    if (ap) {
                        ap(at);
                    }
                }
                aU = false;
            }
        }
    };
} ();
var SWFUpload;
if (SWFUpload == undefined) {
    SWFUpload = function (b) {
        this.initSWFUpload(b);
    };
}
SWFUpload.prototype.initSWFUpload = function (c) {
    try {
        this.customSettings = {};
        this.settings = c;
        this.eventQueue = [];
        this.movieName = "SWFUpload_" + SWFUpload.movieCount++;
        this.movieElement = null;
        SWFUpload.instances[this.movieName] = this;
        this.initSettings();
        this.loadFlash();
        this.displayDebugInfo();
    } catch (d) {
        delete SWFUpload.instances[this.movieName];
        throw d;
    }
};
SWFUpload.instances = {};
SWFUpload.movieCount = 0;
SWFUpload.version = "2.2.0 2009-03-25";
SWFUpload.QUEUE_ERROR = {
    QUEUE_LIMIT_EXCEEDED: -100,
    FILE_EXCEEDS_SIZE_LIMIT: -110,
    ZERO_BYTE_FILE: -120,
    INVALID_FILETYPE: -130
};
SWFUpload.UPLOAD_ERROR = {
    HTTP_ERROR: -200,
    MISSING_UPLOAD_URL: -210,
    IO_ERROR: -220,
    SECURITY_ERROR: -230,
    UPLOAD_LIMIT_EXCEEDED: -240,
    UPLOAD_FAILED: -250,
    SPECIFIED_FILE_ID_NOT_FOUND: -260,
    FILE_VALIDATION_FAILED: -270,
    FILE_CANCELLED: -280,
    UPLOAD_STOPPED: -290
};
SWFUpload.FILE_STATUS = {
    QUEUED: -1,
    IN_PROGRESS: -2,
    ERROR: -3,
    COMPLETE: -4,
    CANCELLED: -5
};
SWFUpload.BUTTON_ACTION = {
    SELECT_FILE: -100,
    SELECT_FILES: -110,
    START_UPLOAD: -120
};
SWFUpload.CURSOR = {
    ARROW: -1,
    HAND: -2
};
SWFUpload.WINDOW_MODE = {
    WINDOW: "window",
    TRANSPARENT: "transparent",
    OPAQUE: "opaque"
};
SWFUpload.completeURL = function (e) {
    if (typeof (e) !== "string" || e.match(/^https?:\/\//i) || e.match(/^\//)) {
        return e;
    }
    var f = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ":" + window.location.port : "");
    var d = window.location.pathname.lastIndexOf("/");
    if (d <= 0) {
        path = "/";
    } else {
        path = window.location.pathname.substr(0, d) + "/";
    }
    return path + e;
};
SWFUpload.prototype.initSettings = function () {
    this.ensureDefault = function (c, d) {
        this.settings[c] = (this.settings[c] == undefined) ? d : this.settings[c];
    };
    this.ensureDefault("upload_url", "");
    this.ensureDefault("preserve_relative_urls", false);
    this.ensureDefault("file_post_name", "Filedata");
    this.ensureDefault("post_params", {});
    this.ensureDefault("use_query_string", false);
    this.ensureDefault("requeue_on_error", false);
    this.ensureDefault("http_success", []);
    this.ensureDefault("assume_success_timeout", 0);
    this.ensureDefault("file_types", "*.*");
    this.ensureDefault("file_types_description", "All Files");
    this.ensureDefault("file_size_limit", 0);
    this.ensureDefault("file_upload_limit", 0);
    this.ensureDefault("file_queue_limit", 0);
    this.ensureDefault("flash_url", "swfupload.swf");
    this.ensureDefault("prevent_swf_caching", true);
    this.ensureDefault("button_image_url", "");
    this.ensureDefault("button_width", 1);
    this.ensureDefault("button_height", 1);
    this.ensureDefault("button_text", "");
    this.ensureDefault("button_text_style", "color: #000000; font-size: 16pt;");
    this.ensureDefault("button_text_top_padding", 0);
    this.ensureDefault("button_text_left_padding", 0);
    this.ensureDefault("button_action", SWFUpload.BUTTON_ACTION.SELECT_FILES);
    this.ensureDefault("button_disabled", false);
    this.ensureDefault("button_placeholder_id", "");
    this.ensureDefault("button_placeholder", null);
    this.ensureDefault("button_cursor", SWFUpload.CURSOR.ARROW);
    this.ensureDefault("button_window_mode", SWFUpload.WINDOW_MODE.WINDOW);
    this.ensureDefault("debug", false);
    this.settings.debug_enabled = this.settings.debug;
    this.settings.return_upload_start_handler = this.returnUploadStart;
    this.ensureDefault("swfupload_loaded_handler", null);
    this.ensureDefault("file_dialog_start_handler", null);
    this.ensureDefault("file_queued_handler", null);
    this.ensureDefault("file_queue_error_handler", null);
    this.ensureDefault("file_dialog_complete_handler", null);
    this.ensureDefault("upload_start_handler", null);
    this.ensureDefault("upload_progress_handler", null);
    this.ensureDefault("upload_error_handler", null);
    this.ensureDefault("upload_success_handler", null);
    this.ensureDefault("upload_complete_handler", null);
    this.ensureDefault("debug_handler", this.debugMessage);
    this.ensureDefault("custom_settings", {});
    this.customSettings = this.settings.custom_settings;
    if (!!this.settings.prevent_swf_caching) {
        this.settings.flash_url = this.settings.flash_url + (this.settings.flash_url.indexOf("?") < 0 ? "?" : "&") + "preventswfcaching=" + new Date().getTime();
    }
    if (!this.settings.preserve_relative_urls) {
        this.settings.upload_url = SWFUpload.completeURL(this.settings.upload_url);
        this.settings.button_image_url = SWFUpload.completeURL(this.settings.button_image_url);
    }
    delete this.ensureDefault;
};
SWFUpload.prototype.loadFlash = function () {
    var d, c;
    if (document.getElementById(this.movieName) !== null) {
        throw "ID " + this.movieName + " is already in use. The Flash Object could not be added";
    }
    d = document.getElementById(this.settings.button_placeholder_id) || this.settings.button_placeholder;
    if (d == undefined) {
        throw "Could not find the placeholder element: " + this.settings.button_placeholder_id;
    }
    c = document.createElement("div");
    c.innerHTML = this.getFlashHTML();
    d.parentNode.replaceChild(c.firstChild, d);
    if (window[this.movieName] == undefined) {
        window[this.movieName] = this.getMovieElement();
    }
};
SWFUpload.prototype.getFlashHTML = function () {
    var obj = ['<object id="', this.movieName, '" type="application/x-shockwave-flash" data="', this.settings.flash_url, '" width="', this.settings.button_width, '" height="', this.settings.button_height, '" class="swfupload">'].join(""),
    params = ['<param name="wmode" value="', this.settings.button_window_mode, '" />', '<param name="movie" value="', this.settings.flash_url, '" />', '<param name="quality" value="high" />', '<param name="menu" value="false" />', '<param name="allowScriptAccess" value="always" />', '<param name="flashvars" value="', this.getFlashVars(), '" />'].join("");
    if (navigator.userAgent.search(/MSIE/) > -1) {
        obj = ['<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" id="', this.movieName, '" width="', this.settings.button_width, '" height="', this.settings.button_height, '" class="swfupload">'].join("");
        params += '<param name="src" value="' + this.settings.flash_url + '" />';
    }
    return [obj, params, '</object>'].join("");
};
SWFUpload.prototype.getFlashVars = function () {
    var c = this.buildParamString();
    var d = this.settings.http_success.join(",");
    return ["movieName=", encodeURIComponent(this.movieName), "&amp;uploadURL=", encodeURIComponent(this.settings.upload_url), "&amp;useQueryString=", encodeURIComponent(this.settings.use_query_string), "&amp;requeueOnError=", encodeURIComponent(this.settings.requeue_on_error), "&amp;httpSuccess=", encodeURIComponent(d), "&amp;assumeSuccessTimeout=", encodeURIComponent(this.settings.assume_success_timeout), "&amp;params=", encodeURIComponent(c), "&amp;filePostName=", encodeURIComponent(this.settings.file_post_name), "&amp;fileTypes=", encodeURIComponent(this.settings.file_types), "&amp;fileTypesDescription=", encodeURIComponent(this.settings.file_types_description), "&amp;fileSizeLimit=", encodeURIComponent(this.settings.file_size_limit), "&amp;fileUploadLimit=", encodeURIComponent(this.settings.file_upload_limit), "&amp;fileQueueLimit=", encodeURIComponent(this.settings.file_queue_limit), "&amp;debugEnabled=", encodeURIComponent(this.settings.debug_enabled), "&amp;buttonImageURL=", encodeURIComponent(this.settings.button_image_url), "&amp;buttonWidth=", encodeURIComponent(this.settings.button_width), "&amp;buttonHeight=", encodeURIComponent(this.settings.button_height), "&amp;buttonText=", encodeURIComponent(this.settings.button_text), "&amp;buttonTextTopPadding=", encodeURIComponent(this.settings.button_text_top_padding), "&amp;buttonTextLeftPadding=", encodeURIComponent(this.settings.button_text_left_padding), "&amp;buttonTextStyle=", encodeURIComponent(this.settings.button_text_style), "&amp;buttonAction=", encodeURIComponent(this.settings.button_action), "&amp;buttonDisabled=", encodeURIComponent(this.settings.button_disabled), "&amp;buttonCursor=", encodeURIComponent(this.settings.button_cursor)].join("");
};
SWFUpload.prototype.getMovieElement = function () {
    if (this.movieElement == undefined) {
        this.movieElement = document.getElementById(this.movieName);
    }
    if (this.movieElement === null) {
        throw "Could not find Flash element";
    }
    return this.movieElement;
};
SWFUpload.prototype.buildParamString = function () {
    var f = this.settings.post_params;
    var d = [];
    if (typeof (f) === "object") {
        for (var e in f) {
            if (f.hasOwnProperty(e)) {
                d.push(encodeURIComponent(e.toString()) + "=" + encodeURIComponent(f[e].toString()));
            }
        }
    }
    return d.join("&amp;");
};
SWFUpload.prototype.destroy = function () {
    try {
        this.cancelUpload(null, false);
        var g = null;
        g = this.getMovieElement();
        if (g && typeof (g.CallFunction) === "unknown") {
            for (var j in g) {
                try {
                    if (typeof (g[j]) === "function") {
                        g[j] = null;
                    }
                } catch (h) { }
            }
            try {
                g.parentNode.removeChild(g);
            } catch (f) { }
        }
        window[this.movieName] = null;
        SWFUpload.instances[this.movieName] = null;
        delete SWFUpload.instances[this.movieName];
        this.movieElement = null;
        this.settings = null;
        this.customSettings = null;
        this.eventQueue = null;
        this.movieName = null;
        return true;
    } catch (i) {
        return false;
    }
};
SWFUpload.prototype.displayDebugInfo = function () {
    this.debug(["---SWFUpload Instance Info---\n", "Version: ", SWFUpload.version, "\n", "Movie Name: ", this.movieName, "\n", "Settings:\n", "\t", "upload_url:               ", this.settings.upload_url, "\n", "\t", "flash_url:                ", this.settings.flash_url, "\n", "\t", "use_query_string:         ", this.settings.use_query_string.toString(), "\n", "\t", "requeue_on_error:         ", this.settings.requeue_on_error.toString(), "\n", "\t", "http_success:             ", this.settings.http_success.join(", "), "\n", "\t", "assume_success_timeout:   ", this.settings.assume_success_timeout, "\n", "\t", "file_post_name:           ", this.settings.file_post_name, "\n", "\t", "post_params:              ", this.settings.post_params.toString(), "\n", "\t", "file_types:               ", this.settings.file_types, "\n", "\t", "file_types_description:   ", this.settings.file_types_description, "\n", "\t", "file_size_limit:          ", this.settings.file_size_limit, "\n", "\t", "file_upload_limit:        ", this.settings.file_upload_limit, "\n", "\t", "file_queue_limit:         ", this.settings.file_queue_limit, "\n", "\t", "debug:                    ", this.settings.debug.toString(), "\n", "\t", "prevent_swf_caching:      ", this.settings.prevent_swf_caching.toString(), "\n", "\t", "button_placeholder_id:    ", this.settings.button_placeholder_id.toString(), "\n", "\t", "button_placeholder:       ", (this.settings.button_placeholder ? "Set" : "Not Set"), "\n", "\t", "button_image_url:         ", this.settings.button_image_url.toString(), "\n", "\t", "button_width:             ", this.settings.button_width.toString(), "\n", "\t", "button_height:            ", this.settings.button_height.toString(), "\n", "\t", "button_text:              ", this.settings.button_text.toString(), "\n", "\t", "button_text_style:        ", this.settings.button_text_style.toString(), "\n", "\t", "button_text_top_padding:  ", this.settings.button_text_top_padding.toString(), "\n", "\t", "button_text_left_padding: ", this.settings.button_text_left_padding.toString(), "\n", "\t", "button_action:            ", this.settings.button_action.toString(), "\n", "\t", "button_disabled:          ", this.settings.button_disabled.toString(), "\n", "\t", "custom_settings:          ", this.settings.custom_settings.toString(), "\n", "Event Handlers:\n", "\t", "swfupload_loaded_handler assigned:  ", (typeof this.settings.swfupload_loaded_handler === "function").toString(), "\n", "\t", "file_dialog_start_handler assigned: ", (typeof this.settings.file_dialog_start_handler === "function").toString(), "\n", "\t", "file_queued_handler assigned:       ", (typeof this.settings.file_queued_handler === "function").toString(), "\n", "\t", "file_queue_error_handler assigned:  ", (typeof this.settings.file_queue_error_handler === "function").toString(), "\n", "\t", "upload_start_handler assigned:      ", (typeof this.settings.upload_start_handler === "function").toString(), "\n", "\t", "upload_progress_handler assigned:   ", (typeof this.settings.upload_progress_handler === "function").toString(), "\n", "\t", "upload_error_handler assigned:      ", (typeof this.settings.upload_error_handler === "function").toString(), "\n", "\t", "upload_success_handler assigned:    ", (typeof this.settings.upload_success_handler === "function").toString(), "\n", "\t", "upload_complete_handler assigned:   ", (typeof this.settings.upload_complete_handler === "function").toString(), "\n", "\t", "debug_handler assigned:             ", (typeof this.settings.debug_handler === "function").toString(), "\n"].join(""));
};
SWFUpload.prototype.addSetting = function (d, f, e) {
    if (f == undefined) {
        return (this.settings[d] = e);
    } else {
        return (this.settings[d] = f);
    }
};
SWFUpload.prototype.getSetting = function (b) {
    if (this.settings[b] != undefined) {
        return this.settings[b];
    }
    return "";
};
SWFUpload.prototype.callFlash = function (functionName, argumentArray) {
    argumentArray = argumentArray || [];
    var movieElement = this.getMovieElement();
    var returnValue, returnString;
    try {
        returnString = movieElement.CallFunction('<invoke name="' + functionName + '" returntype="javascript">' + __flash__argumentsToXML(argumentArray, 0) + "</invoke>");
        returnValue = eval(returnString);
    } catch (ex) {
        //throw "Call to " + functionName + " failed";
    }
    if (returnValue != undefined && typeof returnValue.post === "object") {
        returnValue = this.unescapeFilePostParams(returnValue);
    }
    return returnValue;
};
SWFUpload.prototype.selectFile = function () {
    this.callFlash("SelectFile");
};
SWFUpload.prototype.selectFiles = function () {
    this.callFlash("SelectFiles");
};
SWFUpload.prototype.startUpload = function (b) {
    this.callFlash("StartUpload", [b]);
};
SWFUpload.prototype.cancelUpload = function (d, c) {
    if (c !== false) {
        c = true;
    }
    this.callFlash("CancelUpload", [d, c]);
};
SWFUpload.prototype.stopUpload = function () {
    this.callFlash("StopUpload");
};
SWFUpload.prototype.getStats = function () {
    return this.callFlash("GetStats");
};
SWFUpload.prototype.setStats = function (b) {
    this.callFlash("SetStats", [b]);
};
SWFUpload.prototype.getFile = function (b) {
    if (typeof (b) === "number") {
        return this.callFlash("GetFileByIndex", [b]);
    } else {
        return this.callFlash("GetFile", [b]);
    }
};
SWFUpload.prototype.addFileParam = function (e, d, f) {
    return this.callFlash("AddFileParam", [e, d, f]);
};
SWFUpload.prototype.removeFileParam = function (d, c) {
    this.callFlash("RemoveFileParam", [d, c]);
};
SWFUpload.prototype.setUploadURL = function (b) {
    this.settings.upload_url = b.toString();
    this.callFlash("SetUploadURL", [b]);
};
SWFUpload.prototype.setPostParams = function (b) {
    this.settings.post_params = b;
    this.callFlash("SetPostParams", [b]);
};
SWFUpload.prototype.addPostParam = function (d, c) {
    this.settings.post_params[d] = c;
    this.callFlash("SetPostParams", [this.settings.post_params]);
};
SWFUpload.prototype.removePostParam = function (b) {
    delete this.settings.post_params[b];
    this.callFlash("SetPostParams", [this.settings.post_params]);
};
SWFUpload.prototype.setFileTypes = function (d, c) {
    this.settings.file_types = d;
    this.settings.file_types_description = c;
    this.callFlash("SetFileTypes", [d, c]);
};
SWFUpload.prototype.setFileSizeLimit = function (b) {
    this.settings.file_size_limit = b;
    this.callFlash("SetFileSizeLimit", [b]);
};
SWFUpload.prototype.setFileUploadLimit = function (b) {
    this.settings.file_upload_limit = b;
    this.callFlash("SetFileUploadLimit", [b]);
};
SWFUpload.prototype.setFileQueueLimit = function (b) {
    this.settings.file_queue_limit = b;
    this.callFlash("SetFileQueueLimit", [b]);
};
SWFUpload.prototype.setFilePostName = function (b) {
    this.settings.file_post_name = b;
    this.callFlash("SetFilePostName", [b]);
};
SWFUpload.prototype.setUseQueryString = function (b) {
    this.settings.use_query_string = b;
    this.callFlash("SetUseQueryString", [b]);
};
SWFUpload.prototype.setRequeueOnError = function (b) {
    this.settings.requeue_on_error = b;
    this.callFlash("SetRequeueOnError", [b]);
};
SWFUpload.prototype.setHTTPSuccess = function (b) {
    if (typeof b === "string") {
        b = b.replace(" ", "").split(",");
    }
    this.settings.http_success = b;
    this.callFlash("SetHTTPSuccess", [b]);
};
SWFUpload.prototype.setAssumeSuccessTimeout = function (b) {
    this.settings.assume_success_timeout = b;
    this.callFlash("SetAssumeSuccessTimeout", [b]);
};
SWFUpload.prototype.setDebugEnabled = function (b) {
    this.settings.debug_enabled = b;
    this.callFlash("SetDebugEnabled", [b]);
};
SWFUpload.prototype.setButtonImageURL = function (b) {
    if (b == undefined) {
        b = "";
    }
    this.settings.button_image_url = b;
    this.callFlash("SetButtonImageURL", [b]);
};
SWFUpload.prototype.setButtonDimensions = function (f, e) {
    this.settings.button_width = f;
    this.settings.button_height = e;
    var d = this.getMovieElement();
    if (d != undefined) {
        d.style.width = f + "px";
        d.style.height = e + "px";
    }
    this.callFlash("SetButtonDimensions", [f, e]);
};
SWFUpload.prototype.setButtonText = function (b) {
    this.settings.button_text = b;
    this.callFlash("SetButtonText", [b]);
};
SWFUpload.prototype.setButtonTextPadding = function (c, d) {
    this.settings.button_text_top_padding = d;
    this.settings.button_text_left_padding = c;
    this.callFlash("SetButtonTextPadding", [c, d]);
};
SWFUpload.prototype.setButtonTextStyle = function (b) {
    this.settings.button_text_style = b;
    this.callFlash("SetButtonTextStyle", [b]);
};
SWFUpload.prototype.setButtonDisabled = function (b) {
    this.settings.button_disabled = b;
    this.callFlash("SetButtonDisabled", [b]);
};
SWFUpload.prototype.setButtonAction = function (b) {
    this.settings.button_action = b;
    this.callFlash("SetButtonAction", [b]);
};
SWFUpload.prototype.setButtonCursor = function (b) {
    this.settings.button_cursor = b;
    this.callFlash("SetButtonCursor", [b]);
};
SWFUpload.prototype.queueEvent = function (d, f) {
    if (f == undefined) {
        f = [];
    } else {
        if (!(f instanceof Array)) {
            f = [f];
        }
    }
    var e = this;
    if (typeof this.settings[d] === "function") {
        this.eventQueue.push(function () {
            this.settings[d].apply(this, f);
        });
        setTimeout(function () {
            e.executeNextEvent();
        },
        0);
    } else {
        if (this.settings[d] !== null) {
            throw "Event handler " + d + " is unknown or is not a function";
        }
    }
};
SWFUpload.prototype.executeNextEvent = function () {
    var b = this.eventQueue ? this.eventQueue.shift() : null;
    if (typeof (b) === "function") {
        b.apply(this);
    }
};
SWFUpload.prototype.unescapeFilePostParams = function (l) {
    var j = /[$]([0-9a-f]{4})/i;
    var i = {};
    var k;
    if (l != undefined) {
        for (var h in l.post) {
            if (l.post.hasOwnProperty(h)) {
                k = h;
                var g;
                while ((g = j.exec(k)) !== null) {
                    k = k.replace(g[0], String.fromCharCode(parseInt("0x" + g[1], 16)));
                }
                i[k] = l.post[h];
            }
        }
        l.post = i;
    }
    return l;
};
SWFUpload.prototype.testExternalInterface = function () {
    try {
        return this.callFlash("TestExternalInterface");
    } catch (b) {
        return false;
    }
};
SWFUpload.prototype.flashReady = function () {
    var b = this.getMovieElement();
    if (!b) {
        this.debug("Flash called back ready but the flash movie can't be found.");
        return;
    }
    this.cleanUp(b);
    this.queueEvent("swfupload_loaded_handler");
};
SWFUpload.prototype.cleanUp = function (f) {
    try {
        if (this.movieElement && typeof (f.CallFunction) === "unknown") {
            this.debug("Removing Flash functions hooks (this should only run in IE and should prevent memory leaks)");
            for (var h in f) {
                try {
                    if (typeof (f[h]) === "function") {
                        f[h] = null;
                    }
                } catch (e) { }
            }
        }
    } catch (g) { }
    window.__flash__removeCallback = function (c, b) {
        try {
            if (c) {
                c[b] = null;
            }
        } catch (a) { }
    };
};
SWFUpload.prototype.fileDialogStart = function () {
    this.queueEvent("file_dialog_start_handler");
};
SWFUpload.prototype.fileQueued = function (b) {
    b = this.unescapeFilePostParams(b);
    this.queueEvent("file_queued_handler", b);
};
SWFUpload.prototype.fileQueueError = function (e, f, d) {
    e = this.unescapeFilePostParams(e);
    this.queueEvent("file_queue_error_handler", [e, f, d]);
};
SWFUpload.prototype.fileDialogComplete = function (d, f, e) {
    this.queueEvent("file_dialog_complete_handler", [d, f, e]);
};
SWFUpload.prototype.uploadStart = function (b) {
    b = this.unescapeFilePostParams(b);
    this.queueEvent("return_upload_start_handler", b);
};
SWFUpload.prototype.returnUploadStart = function (d) {
    var c;
    if (typeof this.settings.upload_start_handler === "function") {
        d = this.unescapeFilePostParams(d);
        c = this.settings.upload_start_handler.call(this, d);
    } else {
        if (this.settings.upload_start_handler != undefined) {
            throw "upload_start_handler must be a function";
        }
    }
    if (c === undefined) {
        c = true;
    }
    c = !!c;
    this.callFlash("ReturnUploadStart", [c]);
};
SWFUpload.prototype.uploadProgress = function (e, f, d) {
    e = this.unescapeFilePostParams(e);
    this.queueEvent("upload_progress_handler", [e, f, d]);
};
SWFUpload.prototype.uploadError = function (e, f, d) {
    e = this.unescapeFilePostParams(e);
    this.queueEvent("upload_error_handler", [e, f, d]);
};
SWFUpload.prototype.uploadSuccess = function (d, e, f) {
    d = this.unescapeFilePostParams(d);
    this.queueEvent("upload_success_handler", [d, e, f]);
};
SWFUpload.prototype.uploadComplete = function (b) {
    b = this.unescapeFilePostParams(b);
    this.queueEvent("upload_complete_handler", b);
};
SWFUpload.prototype.debug = function (b) {
    this.queueEvent("debug_handler", b);
};
SWFUpload.prototype.debugMessage = function (h) {
    if (this.settings.debug) {
        var f, g = [];
        if (typeof h === "object" && typeof h.name === "string" && typeof h.message === "string") {
            for (var e in h) {
                if (h.hasOwnProperty(e)) {
                    g.push(e + ": " + h[e]);
                }
            }
            f = g.join("\n") || "";
            g = f.split("\n");
            f = "EXCEPTION: " + g.join("\nEXCEPTION: ");
            SWFUpload.Console.writeLine(f);
        } else {
            SWFUpload.Console.writeLine(h);
        }
    }
};
SWFUpload.Console = {};
SWFUpload.Console.writeLine = function (g) {
    var e, f;
    try {
        e = document.getElementById("SWFUpload_Console");
        if (!e) {
            f = document.createElement("form");
            document.getElementsByTagName("body")[0].appendChild(f);
            e = document.createElement("textarea");
            e.id = "SWFUpload_Console";
            e.style.fontFamily = "monospace";
            e.setAttribute("wrap", "off");
            e.wrap = "off";
            e.style.overflow = "auto";
            e.style.width = "700px";
            e.style.height = "350px";
            e.style.margin = "5px";
            f.appendChild(e);
        }
        e.value += g + "\n";
        e.scrollTop = e.scrollHeight - e.clientHeight;
    } catch (h) {
        alert("Exception: " + h.name + " Message: " + h.message);
    }
};
 (function (c) {
 	var _arr = [];
    var b = {
        init: function (d, e) {
            return this.each(function () {
                var n = c(this);
                var m = n.clone();
                var j = c.extend({
                    id: n.attr("id"),
                    swf: "uploadify.swf",
                    uploader: "uploadify.php",
                    auto: true,
                    buttonClass: "",
                    buttonCursor: "hand",
                    buttonImage: null,
                    buttonText: "SELECT FILES",
                    checkExisting: false,
                    debug: false,
                    fileObjName: "Filedata",
                    fileSizeLimit: 0,
                    fileTypeDesc: "All Files",
                    fileTypeExts: "*.*",
                    height: 30,
                    itemTemplate: false,
                    method: "post",
                    multi: true,
                    formData: {},
                    preventCaching: true,
                    progressData: "percentage",
                    queueID: false,
                    queueSizeLimit: 999,
                    removeCompleted: true,
                    removeTimeout: 3,
                    requeueErrors: false,
                    successTimeout: 30,
                    uploadLimit: 0,
                    width: 120,
                    overrideEvents: [],
                    userImageIDs:[]
                },
                d);
                //sunao
                _arr=j.userImageIDs;
                var g = {
                    assume_success_timeout: j.successTimeout,
                    button_placeholder_id: j.id,
                    button_width: j.width,
                    button_height: j.height,
                    button_text: null,
                    button_text_style: null,
                    button_text_top_padding: 0,
                    button_text_left_padding: 0,
                    button_action: (j.multi ? SWFUpload.BUTTON_ACTION.SELECT_FILES : SWFUpload.BUTTON_ACTION.SELECT_FILE),
                    button_disabled: false,
                    button_cursor: (j.buttonCursor == "arrow" ? SWFUpload.CURSOR.ARROW : SWFUpload.CURSOR.HAND),
                    button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
                    debug: j.debug,
                    requeue_on_error: j.requeueErrors,
                    file_post_name: j.fileObjName,
                    file_size_limit: j.fileSizeLimit,
                    file_types: j.fileTypeExts,
                    file_types_description: j.fileTypeDesc,
                    file_queue_limit: j.queueSizeLimit,
                    file_upload_limit: j.uploadLimit,
                    flash_url: j.swf,
                    prevent_swf_caching: j.preventCaching,
                    post_params: j.formData,
                    upload_url: j.uploader,
                    use_query_string: (j.method == "get"),
                    file_dialog_complete_handler: a.onDialogClose,
                    file_dialog_start_handler: a.onDialogOpen,
                    file_queued_handler: a.onSelect,
                    file_queue_error_handler: a.onSelectError,
                    swfupload_loaded_handler: j.onSWFReady,
                    upload_complete_handler: a.onUploadComplete,
                    upload_error_handler: a.onUploadError,
                    upload_progress_handler: a.onUploadProgress,
                    upload_start_handler: a.onUploadStart,
                    upload_success_handler: a.onUploadSuccess
                };
                if (e) {
                    g = c.extend(g, e);
                }
                g = c.extend(g, j);
                var o = swfobject.getFlashPlayerVersion();
                var h = (o.major >= 9);
                if (h) {
                    window["uploadify_" + j.id] = new SWFUpload(g);
                    var i = window["uploadify_" + j.id];
                    n.data("uploadify", i);
                    var l = c("<div />", {
                        id: j.id,
                        "class": "uploadify",
                        css: {
                            height: j.height + "px",
                            width: j.width + "px"
                        }
                    });
                    c("#" + i.movieName).wrap(l);
                    l = c("#" + j.id);
                    l.data("uploadify", i);
                    var f = c("<div />", {
                        id: j.id + "-button",
                        "class": "uploadify-button " + j.buttonClass
                    });
                    if (j.buttonImage) {
                        f.css({
                            "background-image": "url('" + j.buttonImage + "')",
                            "text-indent": "-9999px"
                        });
                    }
                    f.html('<span class="uploadify-button-text">' + j.buttonText + "</span>").css({
                        height: j.height + "px",
                        "line-height": j.height + "px",
                        width: j.width + "px"
                    });
                    l.append(f);
                    c("#" + i.movieName).css({
                        position: "absolute",
                        "z-index": 1
                    });
                    if (!j.queueID) {
                        var k = c("<div />", {
                            id: j.id + "-queue",
                            "class": "uploadify-queue"
                        });
                        l.after(k);
                        i.settings.queueID = j.id + "-queue";
                        i.settings.defaultQueue = true;
                    }
                    i.queueData = {
                        files: {},
                        filesSelected: 0,
                        filesQueued: 0,
                        filesReplaced: 0,
                        filesCancelled: 0,
                        filesErrored: 0,
                        uploadsSuccessful: 0,
                        uploadsErrored: 0,
                        averageSpeed: 0,
                        queueLength: 0,
                        queueSize: 0,
                        uploadSize: 0,
                        queueBytesUploaded: 0,
                        uploadQueue: [],
                        errorMsg: "Some files were not added to the queue:"
                    };
                    i.original = m;
                    i.wrapper = l;
                    i.button = f;
                    i.queue = k;
                    if (j.onInit) {
                        j.onInit.call(n, i);
                    }
                } else {
                    if (j.onFallback) {
                        j.onFallback.call(n);
                    }
                }
            });
        },
        cancel: function (d, f) {
			//删除图片操作
			/*
			if(typeof _arr != 'undefined') {
				$[EH](_arr, function(i, item) {
					if(item.id == d) {
						_arr.splice(i, 1);
					}
				});
			}
			*/
            var e = arguments;
            this.each(function () {
                var l = c(this),
                i = l.data("uploadify"),
                j = i.settings,
                h = -1;
                if (e[0]) {
                    if (e[0] == "*") {
                        var g = i.queueData.queueLength;
                        c("#" + j.queueID).find(".uploadify-queue-item").each(function () {
                            h++;
                            if (e[1] === true) {
                                i.cancelUpload(c(this).attr("id"), false);
                            } else {
                                i.cancelUpload(c(this).attr("id"));
                            }
                            c(this).find(".data").removeClass("data").html(" - Cancelled");
                            
                            
                            c(this).find(".uploadify-progress-bar").remove();
                            c(this).delay(1000 + 100 * h).fadeOut(500,
                            function () {
                                c(this).remove();
                            });
                        });
                        i.queueData.queueSize = 0;
                        i.queueData.queueLength = 0;
                        if (j.onClearQueue) {
                            j.onClearQueue.call(l, g);
                        }
                    } else {
                        for (var m = 0; m < e.length; m++) {
                            i.cancelUpload(e[m]);
                            c("#" + e[m]).find(".data").removeClass("data").html(" - Cancelled");
                            c("#" + e[m]).find(".uploadify-progress-bar").remove();
                            c("#" + e[m]).delay(1000 + 100 * m).fadeOut(500,
                            function () {
                                c(this).remove();
                            });
                        }
                    }
                } else {
                    var k = c("#" + j.queueID).find(".uploadify-queue-item").get(0);
                    $item = c(k);
                    i.cancelUpload($item.attr("id"));
                    $item.find(".data").removeClass("data").html(" - Cancelled");
                    $item.find(".uploadify-progress-bar").remove();
                    $item.delay(1000).fadeOut(500,
                    function () {
                        c(this).remove();
                    });
                }
            });
        },
        destroy: function () {
            this.each(function () {
                var f = c(this),
                d = f.data("uploadify"),
                e = d.settings;
                d.destroy();
                if (e.defaultQueue) {
                    c("#" + e.queueID).remove();
                }
                c("#" + e.id).replaceWith(d.original);
                if (e.onDestroy) {
                    e.onDestroy.call(this);
                }
                delete d;
            });
        },
        disable: function (d) {
            this.each(function () {
                var g = c(this),
                e = g.data("uploadify"),
                f = e.settings;
                if (d) {
                    e.button.addClass("disabled");
                    if (f.onDisable) {
                        f.onDisable.call(this);
                    }
                } else {
                    e.button.removeClass("disabled");
                    if (f.onEnable) {
                        f.onEnable.call(this);
                    }
                }
                e.setButtonDisabled(d);
            });
        },
        settings: function (e, g, h) {
            var d = arguments;
            var f = g;
            this.each(function () {
                var k = c(this),
                i = k.data("uploadify"),
                j = i.settings;
                if (typeof (d[0]) == "object") {
                    for (var l in g) {
                        setData(l, g[l]);
                    }
                }
                if (d.length === 1) {
                    f = j[e];
                } else {
                    switch (e) {
                        case "uploader":
                            i.setUploadURL(g);
                            break;
                        case "formData":
                            if (!h) {
                                g = c.extend(j.formData, g);
                            }
                            i.setPostParams(j.formData);
                            break;
                        case "method":
                            if (g == "get") {
                                i.setUseQueryString(true);
                            } else {
                                i.setUseQueryString(false);
                            }
                            break;
                        case "fileObjName":
                            i.setFilePostName(g);
                            break;
                        case "fileTypeExts":
                            i.setFileTypes(g, j.fileTypeDesc);
                            break;
                        case "fileTypeDesc":
                            i.setFileTypes(j.fileTypeExts, g);
                            break;
                        case "fileSizeLimit":
                            i.setFileSizeLimit(g);
                            break;
                        case "uploadLimit":
                            i.setFileUploadLimit(g);
                            break;
                        case "queueSizeLimit":
                            i.setFileQueueLimit(g);
                            break;
                        case "buttonImage":
                            i.button.css("background-image", settingValue);
                            break;
                        case "buttonCursor":
                            if (g == "arrow") {
                                i.setButtonCursor(SWFUpload.CURSOR.ARROW);
                            } else {
                                i.setButtonCursor(SWFUpload.CURSOR.HAND);
                            }
                            break;
                        case "buttonText":
                            c("#" + j.id + "-button").find(".uploadify-button-text").html(g);
                            break;
                        case "width":
                            i.setButtonDimensions(g, j.height);
                            break;
                        case "height":
                            i.setButtonDimensions(j.width, g);
                            break;
                        case "multi":
                            if (g) {
                                i.setButtonAction(SWFUpload.BUTTON_ACTION.SELECT_FILES);
                            } else {
                                i.setButtonAction(SWFUpload.BUTTON_ACTION.SELECT_FILE);
                            }
                            break;
                    }
                    j[e] = g;
                }
            });
            if (d.length === 1) {
                return f;
            }
        },
        stop: function () {
            this.each(function () {
                var e = c(this),
                d = e.data("uploadify");
                d.queueData.averageSpeed = 0;
                d.queueData.uploadSize = 0;
                d.queueData.bytesUploaded = 0;
                d.queueData.uploadQueue = [];
                d.stopUpload();
            });
        },
        upload: function () {
            var d = arguments;
            this.each(function () {
                var f = c(this),
                e = f.data("uploadify");
                e.queueData.averageSpeed = 0;
                e.queueData.uploadSize = 0;
                e.queueData.bytesUploaded = 0;
                e.queueData.uploadQueue = [];
                if (d[0]) {
                    if (d[0] == "*") {
                        e.queueData.uploadSize = e.queueData.queueSize;
                        e.queueData.uploadQueue.push("*");
                        e.startUpload();
                    } else {
                        for (var g = 0; g < d.length; g++) {
                            e.queueData.uploadSize += e.queueData.files[d[g]].size;
                            e.queueData.uploadQueue.push(d[g]);
                        }
                        e.startUpload(e.queueData.uploadQueue.shift());
                    }
                } else {
                    e.startUpload();
                }
            });
        }
    };
    var a = {
        onDialogOpen: function () {
            var d = this.settings;
            this.queueData.errorMsg = "Some files were not added to the queue:";
            this.queueData.filesReplaced = 0;
            this.queueData.filesCancelled = 0;
            if (d.onDialogOpen) {
                d.onDialogOpen.call(this);
            }
        },
        onDialogClose: function (d, f, g) {
            var e = this.settings;
            this.queueData.filesErrored = d - f;
            this.queueData.filesSelected = d;
            this.queueData.filesQueued = f - this.queueData.filesCancelled;
            this.queueData.queueLength = g;
            if (c.inArray("onDialogClose", e.overrideEvents) < 0) {
                if (this.queueData.filesErrored > 0) {
                    alert(this.queueData.errorMsg);
                }
            }
            if (e.onDialogClose) {
                e.onDialogClose.call(this, this.queueData);
            }
            if (e.auto) {
                c("#" + e.id).uploadify("upload", "*");
            }
        },
        onSelect: function (h) {
            var i = this.settings;
            var f = {};
            for (var g in this.queueData.files) {
                f = this.queueData.files[g];
                if (f.uploaded != true && f.name == h.name) {
                    //var e = confirm('The file named "' + h.name + '" is already in the queue.\nDo you want to replace the existing item in the queue?');
                    //if (!e) {
                        this.cancelUpload(h.id);
                        this.queueData.filesCancelled++;
                        return false;
//                    } else {
                        //c("#" + f.id).remove();
                       //this.cancelUpload(f.id);
                        //this.queueData.filesReplaced++;
//                    }
                }
            }
            var j = Math.round(h.size / 1024);
            var o = "KB";
            if (j > 1000) {
                j = Math.round(j / 1000);
                o = "MB";
            }
            var l = j.toString().split(".");
            j = l[0];
            if (l.length > 1) {
                j += "." + l[1].substr(0, 2);
            }
            j += o;
            var k = h.name;
            if (k.length > 25) {
                k = k.substr(0, 25) + "...";
            }
            itemData = {
                fileID: h.id,
                instanceID: i.id,
                fileName: k,
                fileSize: j
            };
            if (i.itemTemplate == false) {
            
              i.itemTemplate = '<div id="${fileID}" class="uploadify-queue-item"><div class="cancel"><a id="uploadifyClose" href="javascript:$(\'#${instanceID}\').uploadify(\'cancel\', \'${fileID}\')">X</a></div><span class="data"></span><div class="uploadify-progress com_none"><div class="uploadify-progress-bar"></div></div></div>';
            }

            
            if (c.inArray("onSelect", i.overrideEvents) < 0) {
                itemHTML = i.itemTemplate;
                for (var m in itemData) {
                    itemHTML = itemHTML.replace(new RegExp("\\$\\{" + m + "\\}", "g"), itemData[m]);
                }
                c("#" + i.queueID).append(itemHTML);
            }
            
            this.queueData.queueSize += h.size;
            this.queueData.files[h.id] = h;
            if (i.onSelect) {
                i.onSelect.apply(this, arguments);
            }
        },
        onSelectError: function (d, g, f) {
            var e = this.settings;
            if (c.inArray("onSelectError", e.overrideEvents) < 0) {
//                switch (g) {
//                    case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
//                        if (e.queueSizeLimit > f) {
//                            this.queueData.errorMsg += "\nThe number of files selected exceeds the remaining upload limit (" + f + ").";
//                        } else {
//                            this.queueData.errorMsg += "\nThe number of files selected exceeds the queue size limit (" + e.queueSizeLimit + ").";
//                        }
//                        break;
//                    case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
//                        this.queueData.errorMsg += '\nThe file "' + d.name + '" exceeds the size limit (' + e.fileSizeLimit + ").";
//                        break;
//                    case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
//                        this.queueData.errorMsg += '\nThe file "' + d.name + '" is empty.';
//                        break;
//                    case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
//                        this.queueData.errorMsg += '\nThe file "' + d.name + '" is not an accepted file type (' + e.fileTypeDesc + ").";
//                        break;
//                }
            }
            if (g != SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
                delete this.queueData.files[d.id];
            }
            if (e.onSelectError) {
                e.onSelectError.apply(this, arguments);
            }
        },
        onQueueComplete: function () {
            if (this.settings.onQueueComplete) {
                this.settings.onQueueComplete.call(this, this.settings.queueData);
            }
        },
        onUploadComplete: function (f) {
            var g = this.settings,
            d = this;
            var e = this.getStats();

            this.queueData.queueLength = e==null? 0: e.files_queued;
            if (this.queueData.uploadQueue[0] == "*") {
                if (this.queueData.queueLength > 0) {
                    this.startUpload();
                } else {
                    this.queueData.uploadQueue = [];
                    if (g.onQueueComplete) {
                        g.onQueueComplete.call(this, this.queueData);
                    }
                }
            } else {
                if (this.queueData.uploadQueue.length > 0) {
                    this.startUpload(this.queueData.uploadQueue.shift());
                } else {
                    this.queueData.uploadQueue = [];
                    if (g.onQueueComplete) {
                        g.onQueueComplete.call(this, this.queueData);
                    }
                }
            }
            if (c.inArray("onUploadComplete", g.overrideEvents) < 0) {
                if (g.removeCompleted) {
                    switch (f.filestatus) {
                        case SWFUpload.FILE_STATUS.COMPLETE:
                            setTimeout(function () {
                                if (c("#" + f.id)) {
                                    d.queueData.queueSize -= f.size;
                                    d.queueData.queueLength -= 1;
                                    delete d.queueData.files[f.id];
                                    c("#" + f.id).fadeOut(500,
                                function () {
                                    c(this).remove();
                                });
                                }
                            },
                        g.removeTimeout * 1000);
                            break;
                        case SWFUpload.FILE_STATUS.ERROR:
                            if (!g.requeueErrors) {
                                setTimeout(function () {
                                    if (c("#" + f.id)) {
                                        d.queueData.queueSize -= f.size;
                                        d.queueData.queueLength -= 1;
                                        delete d.queueData.files[f.id];
                                        c("#" + f.id).fadeOut(500,
                                    function () {
                                        c(this).remove();
                                    });
                                    }
                                },
                            g.removeTimeout * 1000);
                            }
                            break;
                    }
                } else {
                    f.uploaded = true;
                }
            }
            if (g.onUploadComplete) {
                g.onUploadComplete.call(this, f);
            }
        },
        onUploadError: function (e, i, h) {
            var f = this.settings;
            var g = "Error";
            switch (i) {
                case SWFUpload.UPLOAD_ERROR.HTTP_ERROR:
                    g = "HTTP Error (" + h + ")";
                    break;
                case SWFUpload.UPLOAD_ERROR.MISSING_UPLOAD_URL:
                    g = "Missing Upload URL";
                    break;
                case SWFUpload.UPLOAD_ERROR.IO_ERROR:
                    g = "IO Error";
                    break;
                case SWFUpload.UPLOAD_ERROR.SECURITY_ERROR:
                    g = "Security Error";
                    break;
                case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                    alert("The upload limit has been reached (" + h + ").");
                    g = "Exceeds Upload Limit";
                    break;
                case SWFUpload.UPLOAD_ERROR.UPLOAD_FAILED:
                    g = "Failed";
                    break;
                case SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND:
                    break;
                case SWFUpload.UPLOAD_ERROR.FILE_VALIDATION_FAILED:
                    g = "Validation Error";
                    break;
                case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
                    g = "Cancelled";
                    this.queueData.queueSize -= e.size;
                    this.queueData.queueLength -= 1;
                    if (e.status == SWFUpload.FILE_STATUS.IN_PROGRESS || c.inArray(e.id, this.queueData.uploadQueue) >= 0) {
                        this.queueData.uploadSize -= e.size;
                    }
                    if (f.onCancel) {
                        f.onCancel.call(this, e);
                    }
                    delete this.queueData.files[e.id];
                    break;
                case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
                    g = "Stopped";
                    break;
            }
            if (c.inArray("onUploadError", f.overrideEvents) < 0) {
                if (i != SWFUpload.UPLOAD_ERROR.FILE_CANCELLED && i != SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED) {
                    c("#" + e.id).addClass("uploadify-error");
                }
                c("#" + e.id).find(".uploadify-progress-bar").css("width", "1px");
                if (i != SWFUpload.UPLOAD_ERROR.SPECIFIED_FILE_ID_NOT_FOUND && e.status != SWFUpload.FILE_STATUS.COMPLETE) {
                    c("#" + e.id).find(".data").html(" - " + g);
                }
            }
            var d = this.getStats();
            this.queueData.uploadsErrored = d.upload_errors;
            if (f.onUploadError) {
                f.onUploadError.call(this, e, i, h, g);
            }
        },
        onUploadProgress: function (g, m, j) {
            var h = this.settings;
            var e = new Date();
            var n = e.getTime();
            var k = n - this.timer;
            if (k > 500) {
                this.timer = n;
            }
            var i = m - this.bytesLoaded;
            this.bytesLoaded = m;
            var d = this.queueData.queueBytesUploaded + m;
            var p = Math.round(m / j * 100);
            var o = "KB/s";
            var l = 0;
            var f = (i / 1024) / (k / 1000);
            f = Math.floor(f * 10) / 10;
            if (this.queueData.averageSpeed > 0) {
                this.queueData.averageSpeed = Math.floor((this.queueData.averageSpeed + f) / 2);
            } else {
                this.queueData.averageSpeed = Math.floor(f);
            }
            if (f > 1000) {
                l = (f * 0.001);
                this.queueData.averageSpeed = Math.floor(l);
                o = "MB/s";
            }
            if (c.inArray("onUploadProgress", h.overrideEvents) < 0) {
                if (h.progressData == "percentage") {
                    c("#" + g.id).find(".data").html(" - " + p + "%");
                } else {
                    if (h.progressData == "speed" && k > 500) {
                        c("#" + g.id).find(".data").html(" - " + this.queueData.averageSpeed + o);
                    }
                }
                c("#" + g.id).find(".uploadify-progress-bar").css("width", p + "%");
            }
            if (h.onUploadProgress) {
                h.onUploadProgress.call(this, g, m, j, d, this.queueData.uploadSize);
            }
        },
        onUploadStart: function (d) {
            var e = this.settings;
            var f = new Date();
            this.timer = f.getTime();
            this.bytesLoaded = 0;
            if (this.queueData.uploadQueue.length == 0) {
                this.queueData.uploadSize = d.size;
            }
            if (e.checkExisting) {
                c.ajax({
                    type: "POST",
                    async: false,
                    url: e.checkExisting,
                    data: {
                        filename: d.name
                    },
                    success: function (h) {
                        if (h == 1) {
                            var g = confirm('A file with the name "' + d.name + '" already exists on the server.\nWould you like to replace the existing file?');
                            if (!g) {
                                this.cancelUpload(d.id);
                                c("#" + d.id).remove();
                                if (this.queueData.uploadQueue.length > 0 && this.queueData.queueLength > 0) {
                                    if (this.queueData.uploadQueue[0] == "*") {
                                        this.startUpload();
                                    } else {
                                        this.startUpload(this.queueData.uploadQueue.shift());
                                    }
                                }
                            }
                        }
                    }
                });
            }
            if (e.onUploadStart) {
                e.onUploadStart.call(this, d);
            }
        },
        onUploadSuccess: function (f, h, d) {
            var g = this.settings;
            var e = this.getStats();
            this.queueData.uploadsSuccessful = e.successful_uploads;
            this.queueData.queueBytesUploaded += f.size;
            if (c.inArray("onUploadSuccess", g.overrideEvents) < 0) {
                c("#" + f.id).find(".data").html(" - Complete");
            }
            if (g.onUploadSuccess) {
                g.onUploadSuccess.call(this, f, h, d);
            }
        }
    };
    c.fn.uploadify = function (d) {
        if (b[d]) {
            return b[d].apply(this, Array.prototype.slice.call(arguments, 1));
        } else {
            if (typeof d === "object" || !d) {
                return b.init.apply(this, arguments);
            } else {
                c.error("The method " + d + " does not exist in $.uploadify");
            }
        }
    };
})($);