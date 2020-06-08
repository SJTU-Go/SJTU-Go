Page({
  data: {
    polyline:[],
    routeplan:[]
  },

  onLoad: function (options) {
    var that = this
    var index =options.index
    wx.getStorage({
      key: 'history',
      success(res){var q =new Array(0)
        q.push(res.data[index])
        that.setData({
        routeplan:q
      })}
    })
    wx.getStorage({
      key: 'historyroute',
      success(res){    that.setData({
        polyline:res.data[index]
      })}
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
