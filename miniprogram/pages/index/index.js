//index.js
const app = getApp()

Page({

  data:{
    bus:{},
    walk:{},
    searchtxt:''},
  navigatePage:function()
  {    
  },
  searchInput:function(e)
  {    app.globalData.search =e.detail.value
    
  },
  search:function()
  {      var that =this;
         var tem;
    wx.request({
      url: 'https://api.ltzhou.com/navigate/bus',
      method:'POST',
      header: {
        'content-type': 'application/json'
      },
      data:{
        "arrivePlace": "POINT (121.435505 31.026303)",
        "beginPlace": "图书馆",
        "departTime": "2020/05/11 12:05:12",
        "passPlaces": [
          "学生服务中心"
        ]
      },

      success (res) {
        tem = res.data
        console.log(tem)
        that.setData({bus:tem})
      }}     )


    wx.request({
      url: 'https://api.ltzhou.com/navigate/walk',
      method:'POST',
      header: {
        'content-type': 'application/json'
      },
      data:{
        "arrivePlace": "POINT (121.435505 31.026303)",
        "beginPlace": "图书馆",
        "departTime": "2020/05/11 12:05:12",
        "passPlaces": [
          "学生服务中心"
        ]
      },

      success (res) {
        tem = res.data
        console.log(tem)
        that.setData({walk:tem})
      }}     )
wx.navigateTo({
  url: '../searchresultPage/searchresultPage',
})

  }
})
