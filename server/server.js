var http = require('http');
var url = require('url');
var qs = require('querystring');

var server = http.createServer(function (req, res) {
    var body = "";

    //先从路径中拿参数
    var arg = url.parse(req.url).query;
    //把参数转换成键值对，再从中拿值
    var nameValue = qs.parse(arg)['name'];
    //打印出来是价值对
    var params = qs.parse(arg);
    console.log(qs.parse(arg));
    //打印出来是值
    console.log(nameValue);
    var json ;
    if (params.account == 'lukemi' && params.pwd == 1) {
          json = { 'rsm': 1, 'data': { 'msg': 'success' } };
    }else{
        json = { 'rsm': 0, 'data': { 'msg': 'err' } };
    }
    res.writeHead(200, { "Content-Type": "text/plain;charset=utf-8" });
    res.write(JSON.stringify(json));
    res.end();
});

server.listen(3030);
