// pages/feedback/feedback.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    userID:1,
    tripid:1,
    road:"4",
    vehicle:"4",
    congestion:"4",
    service:"4",
    comment:"",
    polyline:[]
  },
  commentInput:function(e){
    this.setData({comment:e.detail.value})
  },
  congestionInput:function(e){
    this.setData({congestion:e.detail.value})
  },
  serviceInput:function(e){
    this.setData({service:e.detail.value})
  },
  vehicleInput:function(e){
    this.setData({vehicle:e.detail.value})
  },
  roadInput:function(e){
    this.setData({road:e.detail.value})
  },
  onLoad: function (options) {
    var that =this
    var id = options.RT
    console.log(id)
    wx.getStorage({
      key: 'tripID',
      success(res){that.setData({tripid:res.data[id]})
    
      console.log(res.data[id])
    }
    })
    wx.getStorage({
      key: 'userID',
      success(res){that.setData({userID:res.data})
    
      console.log(res.data)
    }
    })
  },

  formSubmit: function (e) {

  //  wx.navigateTo({ url: '../commentmap/commentmap?RT='+JSON.stringify(this.data.polyline),})

var updata = {}
updata.contents = this.data.comment
updata.pickupFB = this.data.vehicle
updata.serviceFB = this.data.service
updata.parkFB = this.data.congestion
updata.trafficFB = this.data.road
updata.tripID = this.data.tripid
updata.userID = this.data.userID
console.log(updata)
wx.request({
  url:'https://api.ltzhou.com/feedback/add',
  data:updata,
  method:"POST",
  success(res){console.log(res.data)}
})
  wx.showToast({
     title: '评论提交成功',
     icon: 'success',
     duration: 2000,
     success: function () {
     setTimeout(function () {
     wx.reLaunch({
     url: '../index/index',
     })
     }, 2000);
     }
    })
},

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})