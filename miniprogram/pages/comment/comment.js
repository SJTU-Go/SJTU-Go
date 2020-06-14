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
  //url: 'https://api.ltzhou.com/comments/getcomments/loc?location=POINT(121.437600 31.025940)',
  url: 'https://api.ltzhou.com/comments/getcomments/loc?location=POINT (121.44344818099999 31.029707975899999)',
  method: 'POST',
  header: {'Content-type': 'application/x-www-form-urlencoded'},
  success: function (res) {
    console.log("daing back")
    console.log(res.data)
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
        var pushlist=res.data[j]
        var lat1 = 31.025940
        var lng1 = 121.437600
        var lat2 = res.data[j].location.coordinates[1]
        var lng2 = res.data[j].location.coordinates[0]
        var radLat1 = lat1*Math.PI / 180.0;
        var radLat2 = lat2*Math.PI / 180.0;
        var a = radLat1 - radLat2;
        var b = lng1*Math.PI / 180.0 - lng2*Math.PI / 180.0;
        var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
        Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s *6378.137 ;// EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
      pushlist.distance = s*1000
      ress.push(pushlist)
      }
      console.log("ressinggg")
      console.log(ress)
      that.setData({reslist:ress})
      wx.setStorage({
        data:ress,
        key: 'commentlist',
      })
  }
  });
},



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
navigatePage:function()
{wx.navigateTo(
  {
  url: '../mapview/mapview'}
)
},
})