Page({
  data: {
    polyline:[],
    routeplan:[]
    // markers: [{
    //   iconPath: "/mark/19.PNG",
    //   id: 0,
    //   latitude: 31.021807,//31.029236,
    //   longitude: 121.429846,//121.452591,
    //   width: 50,
    //   height: 50,
    // },
    // {
    //   iconPath: "/mark/15.PNG",
    //   id: 0,
    //   latitude: 31.021642,//31.029236,
    //   longitude: 121.432335,//121.452591,
    //   width: 50,
    //   height: 50,
    // },
    // {
    //   iconPath: "/mark/7.PNG",
    //   id: 0,
    //   latitude: 31.020502,//31.029236,
    //   longitude: 121.434009,//121.452591,
    //   width: 50,
    //   height: 50,
    // }
    // ],
  },

  onLoad: function (options) {
    this.setData({
      polyline: JSON.parse(options.RT),
      routeplan:JSON.parse(options.plan)
    })
  },

  formSubmit:function(e){
    wx.showToast({
       title: '评论提交成功',
       icon: 'success',
       duration: 2000,
       success: function () {
       setTimeout(function () {
       wx.navigateTo({
       url: '../HistoryPage/HistoryPage',
       })
       }, 2000);
       }
      })
  },

  //显示弹框
  showModal: function () {
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
      showModalStatus: true
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export()
      })
    }.bind(this), 200)
  },

  //隐藏弹框
  hideModal: function () {
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export(),
        showModalStatus: false
      })
    }.bind(this), 200)
  },
})
