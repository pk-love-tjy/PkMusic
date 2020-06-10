function abc(){
    SpiderApi.showToast("这是abc里执行 ");
    SpiderApi.debugLog("这是abc里执行 ");
};
var Spider = function(attachData = null, callback = null, recvCallBack = null, strAddress = '127.0.0.1') {

    if (typeof(WebSocket) != "function") {
        alert("抱歉SpiderJS无法兼容您的浏览器！");
        return;
    }

    var _SERVER_ADDRESS = 'http://' + strAddress + '/';
    /** 接口对象 */
    var spiderObje = null;
    /** websocket接口对象*/
    // var ws = new WebSocket('ws://' + strAddress + ':2020/msg');
    /** 存在异步的Api */
    const asynchronousApi = [
        'adbBack', 'adbHome','adbDevices', 'adbKeyword', 'adbAutoInputkNodeEx', 'adbInputNodeEx', 'adbInputEx', 'adbAutoInputkNode', 'adbInputNode',
        'adbInput', 'adbAutoClickNode', 'adbSlide', 'adbClickNode', 'adbClick', 'adbHeight', 'adbWidth', 'adbXML','adbLog','adbToast',"adbPwd",
        "adbReadFile","adbWriteFile","adbDelFile","adbMakeFile","adbMakeDir","adbRecents","adbScreen","adbLockScreen"，"adbPowerLong"
    ];

    /** 连接成功,并发送消息注册身份*/

    console.log("spiderJS create success.");
    var isMontior = false;

    if (typeof(attachData) == 'function') {
        callback = attachData;
        attachData = null;
    }

    if (typeof(callback) != 'function') {
//        ws.close();
        console.log('spider error, callback not define function!');
        return;
    }

    if (typeof(recvCallBack) == 'function') {
        isMontior = true;
    }
    class _Spider {
            constructor() {

                console.log("欢迎使用SpiderJS");
            };


            /** 延迟函数 */
            sleep (time) {
                return new Promise(resolve => {
                    setTimeout(() => {
                        console.log('等待', time / 1000, '秒');
                        resolve(time);
                    }, time)
                });
            };

            /** Base64解码 */
            base64Decode (data) {
                return Base64.decode(data);
            }

            /** Base64编码 */
            base64Encode (data) {
                return Base64.encode(data);
            }

            //获取控件信息
            adbXML () {
                var data = SpiderApi.getXML();
                var dom = document.createElement('div');
                dom.innerHTML = data;
                return dom;
            };

            //取屏幕宽度
            adbWidth () {
                return SpiderApi.getDeviceWidth();
            };
            //取屏幕高度
            adbHeight () {
                return SpiderApi.getDeviceHeight();
            };
            //模拟点击
            adbClick ( x, y) {
                SpiderApi.click(parseInt(x), parseInt(y))
            };

            //点击控件
            adbClickNode ( node) {
                if (node == null || node == '') {
                        console.log('控件节点可能不正确');
                        return 'error';
                }
                var x = node.getAttribute('x');
                var y = node.getAttribute('y');
                var w = node.getAttribute('w');
                var h = node.getAttribute('h');
                SpiderApi.clickNonde(parseInt(x),parseInt(y),parseInt(w),parseInt(h));
            };


            //滑动屏幕
            adbSlide ( sx, sy, ex, ey, time, timeout) {
                if (time == null) {
                    time = 10;
                }
                if (timeout) {
                    timeout = 0;
                }
                setTimeout(() => {
                    SpiderApi.slide(parseInt(sx), parseInt(sy), parseInt(ex), parseInt(ey), parseInt(time));
                }, timeout);


            };



            /* 三合一 点击控件 1.获取布局, 2.查找节点, 3.点击节点 */
            adbAutoClickNode ( selector) {
                let dom = adbXML(device);
                let node = query(dom, selector);
                adbClickNode(node);
            };


            //模拟输入
            adbInput ( x, y, text) {

            };


            //模拟输入
            adbInputNode ( node, text) {
                if (node == null || node == '') {
                    console.log('控件节点可能不正确');
                    return 'error';
                }
                var x = node.getAttribute('x');
                var y = node.getAttribute('y');
                var w = node.getAttribute('w');
                var h = node.getAttribute('h');
                SpiderApi.input(parseInt(x),parseInt(y),parseInt(w),parseInt(h),text);

            };

            //三合一 模拟输入
            adbAutoInputkNode ( selector, text) {
                let dom =  adbXML();
                let node = query(dom, selector);
                adbInputNode(node,text);
            };
            /** 模拟按键 */
            adbKeyword ( code) {

            };

            /** 后退 */
            adbBack () {
                SpiderApi.back();
            };
            /**返回桌面 */
            adbHome () {
                SpiderApi.home();
            };
            /**最近任务*/
            adbRecents(){
                SpiderApi.recents()
            }
            /**截屏*/
            adbScreen(){
                SpiderApi.screen();
            }
            /**锁屏*/
           adbLockScreen(){
            SpiderApi.lockScreen();
           }
           /**长按锁屏*/
           adbPowerLong(){
           SpiderApi.powerLong()
           }
            /**显示土司 */
            adbToast ( text) {
                SpiderApi.showToast(text);

            }
            adbLog ( text) {
                SpiderApi.debugLog(text);
            }
            /**获取包名绝对路径*/
            adbPwd(){
                return SpiderApi.pwd();
            }
            /**读取文本文件*/
            adbReadFile(path){
                SpiderApi.readFile(path);
            }
            /**写文本文件*/
            adbWriteFile(path,text){
                SpiderApi.writeFile(path,text);
            }
            /**删除文件*/
            adbDelFile(path){
                SpiderApi.delFile(path);
            }
            /**新建文件*/
            adbMakeFile(path){
                SpiderApi.makeFile(path)
            }
            /**新建文件夹*/
            adbMakeDir(){
                SpiderApi.makeDir(path)
            }





        };
    spiderObje = new _Spider();

    if (String(callback).indexOf('async') != -1) {
        callback = String(callback);
        // console.log(callback.replace(/\s*\(([a-zA-Z_$][\w_$]*)\)/g));
        for (var i = 0; i < asynchronousApi.length; i++) {
            let t = asynchronousApi[i];
            eval("var re = /spi\\." + t + "/g");
            callback = callback.replace(re, 'await spi.' + t);
            // console.log(callback);
        }
        callback = eval(callback);
    }

    if (typeof(callback) == 'function') {
        callback(spiderObje, attachData);
    }




    query = function(obj, select) {
        console.log(select);
        var doc = obj.querySelector(select);
        return doc;
    };

    queryAll = function(obj, select) {
        console.log(select);
        var doc = obj.querySelectorAll(select);
        return doc;
    };

    ajax = function(url, fn) {
        var xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.onreadystatechange = function() {
            // readyState == 4说明请求已完成
            if (xhr.readyState == 4) {
                if (xhr.status == 200 || xhr.status == 304) {
                    fn(xhr.responseText);
                } else {
                    // console.log(xhr.status);
                    console.log('error');
                }
            }
        }
        xhr.send();
    };

    ajaxpost = function(url, data, fn) {
        var xhr = new XMLHttpRequest();
        xhr.open('POST', url, true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.onreadystatechange = function() {
            // readyState == 4说明请求已完成
            if (xhr.readyState == 4) {
                if (xhr.status == 200 || xhr.status == 304) {
                    fn(xhr.responseText);
                } else {
                    // console.log(xhr.status);
                    console.log('error');
                }
            }
        }
        xhr.send(data);
    };


    return spiderObje;
}


/*
 *  base64.js
 *
 *  Licensed under the BSD 3-Clause License.
 *    http://opensource.org/licenses/BSD-3-Clause
 *
 *  References:
 *    http://en.wikipedia.org/wiki/Base64
 */
;
(function(global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ?
        module.exports = factory(global) :
        typeof define === 'function' && define.amd ?
        define(factory) : factory(global)
}((
    typeof self !== 'undefined' ? self :
    typeof window !== 'undefined' ? window :
    typeof global !== 'undefined' ? global :
    this
), function(global) {
    'use strict';
    // existing version for noConflict()
    global = global || {};
    var _Base64 = global.Base64;
    var version = "2.5.2";
    // if node.js and NOT React Native, we use Buffer
    var buffer;
    if (typeof module !== 'undefined' && module.exports) {
        try {
            buffer = eval("require('buffer').Buffer");
        } catch (err) {
            buffer = undefined;
        }
    }
    // constants
    var b64chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/';
    var b64tab = function(bin) {
        var t = {};
        for (var i = 0, l = bin.length; i < l; i++) t[bin.charAt(i)] = i;
        return t;
    }(b64chars);
    var fromCharCode = String.fromCharCode;
    // encoder stuff
    var cb_utob = function(c) {
        if (c.length < 2) {
            var cc = c.charCodeAt(0);
            return cc < 0x80 ? c :
                cc < 0x800 ? (fromCharCode(0xc0 | (cc >>> 6)) +
                    fromCharCode(0x80 | (cc & 0x3f))) :
                (fromCharCode(0xe0 | ((cc >>> 12) & 0x0f)) +
                    fromCharCode(0x80 | ((cc >>> 6) & 0x3f)) +
                    fromCharCode(0x80 | (cc & 0x3f)));
        } else {
            var cc = 0x10000 +
                (c.charCodeAt(0) - 0xD800) * 0x400 +
                (c.charCodeAt(1) - 0xDC00);
            return (fromCharCode(0xf0 | ((cc >>> 18) & 0x07)) +
                fromCharCode(0x80 | ((cc >>> 12) & 0x3f)) +
                fromCharCode(0x80 | ((cc >>> 6) & 0x3f)) +
                fromCharCode(0x80 | (cc & 0x3f)));
        }
    };
    var re_utob = /[\uD800-\uDBFF][\uDC00-\uDFFFF]|[^\x00-\x7F]/g;
    var utob = function(u) {
        return u.replace(re_utob, cb_utob);
    };
    var cb_encode = function(ccc) {
        var padlen = [0, 2, 1][ccc.length % 3],
            ord = ccc.charCodeAt(0) << 16 |
            ((ccc.length > 1 ? ccc.charCodeAt(1) : 0) << 8) |
            ((ccc.length > 2 ? ccc.charCodeAt(2) : 0)),
            chars = [
                b64chars.charAt(ord >>> 18),
                b64chars.charAt((ord >>> 12) & 63),
                padlen >= 2 ? '=' : b64chars.charAt((ord >>> 6) & 63),
                padlen >= 1 ? '=' : b64chars.charAt(ord & 63)
            ];
        return chars.join('');
    };
    var btoa = global.btoa ? function(b) {
        return global.btoa(b);
    } : function(b) {
        return b.replace(/[\s\S]{1,3}/g, cb_encode);
    };
    var _encode = function(u) {
        var isUint8Array = Object.prototype.toString.call(u) === '[object Uint8Array]';
        return isUint8Array ? u.toString('base64') :
            btoa(utob(String(u)));
    }
    var encode = function(u, urisafe) {
        return !urisafe ?
            _encode(u) :
            _encode(String(u)).replace(/[+\/]/g, function(m0) {
                return m0 == '+' ? '-' : '_';
            }).replace(/=/g, '');
    };
    var encodeURI = function(u) { return encode(u, true) };
    // decoder stuff
    var re_btou = /[\xC0-\xDF][\x80-\xBF]|[\xE0-\xEF][\x80-\xBF]{2}|[\xF0-\xF7][\x80-\xBF]{3}/g;
    var cb_btou = function(cccc) {
        switch (cccc.length) {
            case 4:
                var cp = ((0x07 & cccc.charCodeAt(0)) << 18) |
                    ((0x3f & cccc.charCodeAt(1)) << 12) |
                    ((0x3f & cccc.charCodeAt(2)) << 6) |
                    (0x3f & cccc.charCodeAt(3)),
                    offset = cp - 0x10000;
                return (fromCharCode((offset >>> 10) + 0xD800) +
                    fromCharCode((offset & 0x3FF) + 0xDC00));
            case 3:
                return fromCharCode(
                    ((0x0f & cccc.charCodeAt(0)) << 12) |
                    ((0x3f & cccc.charCodeAt(1)) << 6) |
                    (0x3f & cccc.charCodeAt(2))
                );
            default:
                return fromCharCode(
                    ((0x1f & cccc.charCodeAt(0)) << 6) |
                    (0x3f & cccc.charCodeAt(1))
                );
        }
    };
    var btou = function(b) {
        return b.replace(re_btou, cb_btou);
    };
    var cb_decode = function(cccc) {
        var len = cccc.length,
            padlen = len % 4,
            n = (len > 0 ? b64tab[cccc.charAt(0)] << 18 : 0) |
            (len > 1 ? b64tab[cccc.charAt(1)] << 12 : 0) |
            (len > 2 ? b64tab[cccc.charAt(2)] << 6 : 0) |
            (len > 3 ? b64tab[cccc.charAt(3)] : 0),
            chars = [
                fromCharCode(n >>> 16),
                fromCharCode((n >>> 8) & 0xff),
                fromCharCode(n & 0xff)
            ];
        chars.length -= [0, 0, 2, 1][padlen];
        return chars.join('');
    };
    var _atob = global.atob ? function(a) {
        return global.atob(a);
    } : function(a) {
        return a.replace(/\S{1,4}/g, cb_decode);
    };
    var atob = function(a) {
        return _atob(String(a).replace(/[^A-Za-z0-9\+\/]/g, ''));
    };
    var _decode = buffer ?
        buffer.from && Uint8Array && buffer.from !== Uint8Array.from ?

        function(a) {
            return (a.constructor === buffer.constructor ?
                a : buffer.from(a, 'base64')).toString();
        } :
        function(a) {
            return (a.constructor === buffer.constructor ?
                a : new buffer(a, 'base64')).toString();
        } :
        function(a) { return btou(_atob(a)) };
    var decode = function(a) {
        return _decode(
            String(a).replace(/[-_]/g, function(m0) { return m0 == '-' ? '+' : '/' })
            .replace(/[^A-Za-z0-9\+\/]/g, '')
        );
    };
    var noConflict = function() {
        var Base64 = global.Base64;
        global.Base64 = _Base64;
        return Base64;
    };
    // export Base64
    global.Base64 = {
        VERSION: version,
        atob: atob,
        btoa: btoa,
        fromBase64: decode,
        toBase64: encode,
        utob: utob,
        encode: encode,
        encodeURI: encodeURI,
        btou: btou,
        decode: decode,
        noConflict: noConflict,
        __buffer__: buffer
    };
    // if ES5 is available, make Base64.extendString() available
    if (typeof Object.defineProperty === 'function') {
        var noEnum = function(v) {
            return { value: v, enumerable: false, writable: true, configurable: true };
        };
        global.Base64.extendString = function() {
            Object.defineProperty(
                String.prototype, 'fromBase64', noEnum(function() {
                    return decode(this)
                }));
            Object.defineProperty(
                String.prototype, 'toBase64', noEnum(function(urisafe) {
                    return encode(this, urisafe)
                }));
            Object.defineProperty(
                String.prototype, 'toBase64URI', noEnum(function() {
                    return encode(this, true)
                }));
        };
    }
    //
    // export Base64 to the namespace
    //
    if (global['Meteor']) { // Meteor.js
        Base64 = global.Base64;
    }
    // module.exports and AMD are mutually exclusive.
    // module.exports has precedence.
    if (typeof module !== 'undefined' && module.exports) {
        module.exports.Base64 = global.Base64;
    } else if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define([], function() { return global.Base64 });
    }
    // that's it!
    return { Base64: global.Base64 }
}));
