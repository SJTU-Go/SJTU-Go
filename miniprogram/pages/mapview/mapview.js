// pages/walk/walk.js

Page({
  data: {
    boxshow:true,
 markers:[],
 item:{},
  },

onLoad:function(){
  var markset=[];
  var that = this;
wx.getStorage({
  key: 'commentlist',
  success(res)
  {for(var i in res.data)
    {var mar={};
      mar.iconPath = "../../images/comment2.png"
      mar.id = i
      mar.latitude = res.data[i].location.coordinates[1]
      mar.longitude = res.data[i].location.coordinates[0]
      mar.width=50
      mar.height=50
markset.push(mar)
    }
console.log(markset)
that.setData({markers:markset})

  }
})
},
showModal: function (e) {
  var that = this
  console.log(e.markerId)
  var iid = e.markerId
  wx.getStorage({
    key: 'commentlist',
    success(res){
      that.setData({item:res.data[iid]})
      that.setData({currentdata:e.markerId})
  var animation = wx.createAnimation({
    duration: 200,
    timingFunction: "linear",
    delay: 0
  })
  that.animation = animation
  animation.translateY(300).step()
  that.setData({
    animationData: animation.export(),
    showModalStatus: true
  })
  setTimeout(function () {
    animation.translateY(0).step()
    that.setData({
      animationData: animation.export()
    })
  }.bind(that), 200)

}
})
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
navigateback:function()
{console.log("nback")
  wx.switchTab({
    url: '../comment/comment',
  })
}
})