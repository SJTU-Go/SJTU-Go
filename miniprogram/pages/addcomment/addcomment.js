// pages/walk/walk.js

Page({
  data: {
    commentInput:'',
    boxshow:true,
    datapass:'',
    // 搜索框状态
    inputShowed: true,
    //显示结果view的状态
    viewShowed: false,
    // 搜索框值
    inputVal: "",
    lista:Array(0),
    fakelist:[{distance:"100m",name:"上院西侧",content:"评论内容",username:"小王"}]
  },
  onLoad: function(options){
    var d = JSON.parse(options.RT)
  var that = this
  wx.getStorage({
    key: 'commentlist',
    success:function(res){
      console.log(res.data[d])
      var ress=new Array(0)
      ress.push(res.data[d])
      console.log("logging")
      that.setData({lista:ress})
      console.log(that.data.lista[0])
    }
  })
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
      console.log(res)
    }

  });
}

,
viewcomment:function()
{console.log("tap")

},
commentInput :function (e) { 
  this.setData({ 
    commentInput:e.detail.value 
    }) 
  },
  updataInput :function () { 
    console.log("updating")
    console.log(this.data.commentInput)

    },
})