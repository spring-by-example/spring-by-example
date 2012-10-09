/*
    Convert *.jsb3 package syntax
*/
function jsbToTestLib(root, pkg){
    var libs = []
    for (var i=0; i < pkg.files.length; i++) {
        if (!pkg.files[i]) continue;
        libs.push({
            type: "js",
            src: root + pkg.files[i].path + pkg.files[i].name
        })
    }
    return libs
}
