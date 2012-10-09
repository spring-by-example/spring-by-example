if (system.args.length !== 1) {
    system.print('Usage:');
    system.print('  hammerjs build-data.js');
    system.exit(-1);
}

var scanDirectory = function (path) {
    var entries = [],
        subdirs;
    if (fs.exists(path) && fs.isFile(path) && path.match('.js$')) {
        entries.push(path);
    } else if (fs.isDirectory(path)) {
        fs.list(path).forEach(function (e) {
            subdirs = scanDirectory(path + '/' + e);
            subdirs.forEach(function (s) {
                entries.push(s);
            });
        });
    }
    return entries;
};
var specsFolder = ['../spec' ],
specs = [];
for (var i = 0; i < specsFolder.length; i++) {
    specs = specs.concat(scanDirectory(specsFolder[i]));
}

var output = [];

for (var i = 0; i < specs.length; i++) {
    output.push("'"+specs[i].replace('../', '')+"'");
}

system.print("/* DO NO EDIT THIS FILE MANUALLY IT IS GENERATED AUTOMATICALLY BY ../build/build.sh */\n this.ExtSpecs = [" + output.join(",") + "];");