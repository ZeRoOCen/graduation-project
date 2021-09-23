//require.context():需要一次性的引入某文件夹下的所有文件，那么这个时候就可以使用require.context()来完成
//require.context(directory,useSubdirectories,regExp)，其中三个形参的含义：
//  directory：需要引入文件的目录。'.'表示引入当前目录下的所有文件，此题有三个(./app.js ./index.js ./user.js)； '..'表示引入父目录下的所有文件
//  useSubdirectories：是否查找该目录下的子级目录，如果没有则输入false
//  regExp：匹配引入文件的正则表达式。此题匹配以 .js 结尾的所有路径
const files = require.context('.', false, /\.js$/)

//导出的files有三个属性：resolve、keys、id
//files.keys()：是一个函数，它返回包含所有模块名的数组。  (require.context('.', false, /\.js$/)拼接而成的所有的路径)
//此题数组内容：Array(3) [ "./app.js", "./index.js", "./user.js" ]
const keyArr = files.keys()

const modules = [] //把获取到的目录保存到modules数组中

for(let i=0; i<keyArr.length; i++){
    if (keyArr[i] === './index.js') continue; //如果碰到./index.js就直接进行下一次循环（此题只需要app.js和user.js里面的路径）
    
    // Object { getMenus: {…} }   详细内容(下同)：getMenus: Object { method: "get", url: "/menus" }
    // Object { alluser: {…}, adduser: {…}, deleteuser: {…}, getupdateuser: {…}, edituser: {…}, userstate: {…} }
    const item = files(keyArr[i]).default //把获取到的路径保存到item中

    modules.push({ ...item }) //把对象转换成数组保存
}

export default modules
