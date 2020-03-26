function GetQueryString(name) {
    var url = decodeURI(window.location.search);
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = url.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}

function GetListFromStrInSplit(str) {
    if (!str) return [""]
    if (str == "") return [""]

    var strForSplit = str
    if (str.lastIndexOf(',') == str.length - 1) {
        strForSplit = str.substring(0, str.lastIndexOf(','))
    }
    var list = strForSplit.split(",")
    if (list.length <= 0) {
        return [""]
    }
    return list
}







