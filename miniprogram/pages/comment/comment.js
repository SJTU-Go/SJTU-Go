// pages/walk/walk.js

Page({
  data: {
    boxshow:true,
    datapass:'',
    // 搜索框状态
    inputShowed: true,
    //显示结果view的状态
    viewShowed: false,
    // 搜索框值
    inputVal: "",
    reslist:Array(0),
    list:Array(0),
    fakelist:[{distance:"100m",name:"上院西侧",content:"评论内容",username:"小王"},{distance:"100m",name:"上院西侧",content:"评论内容",username:"小王"},{distance:"100m",name:"上院西侧",content:"评论内容",username:"小王"}]
  },
onLoad:function(e){
  var that = this
  wx.request({
    url: 'https://api.ltzhou.com/comments/loc',
    data: {location:"POINT(121.437600 31.025940)"} ,
    method: 'POST',
    header: {
      'Content-type': 'application/x-www-form-urlencoded'
    },
    success: function (res) {
      console.log(res[0])
      wx.setStorage({
        data: res.data,
        key: 'test',
      })
      that.setData({
        list: res.data
      })
      var i;
      var j;
      var ress=that.data.reslist
      for( j in res.data){
    
      for (i in res.data[j]){
        ress.push(res.data[j][i])

      }
  
      }
      console.log(ress)
      that.setData({reslist:ress})
      wx.setStorage({
        data:ress,
        key: 'commentlist',
      })
    }

  });
}

,
viewcomment:function(e)
{console.log("tap")
console.log("dsadasd")
console.log(e.currentTarget.dataset.ind)
var i =e.currentTarget.dataset.ind
wx.navigateTo(
  {
  url: '../addcomment/addcomment?RT='+JSON.stringify(i) }
)
},
})