//index.js
const app = getApp()

Page({
  data:
  {preference:['筋斗云','哈罗单车','摩拜单车'],
   banned:['旋风100','公交车','步行']
  },
  onLoad: function (options) {
    var that =this;
    wx.getStorage({
    key: 'preference',
    success: function(res){
    that.setData({
      preference:res.data,
    })
    if(that.data.preference.length==0){
    that.setData({
    status:false
    });
    }else{
    that.setData({
    status:true
    })
    }
    },
    fail: function(res) {
    console.log(res+'aaaaa')
    }
    });
    wx.getStorage({
      key: 'banned',
      success: function(res){
      that.setData({
        banned:res.data,
      })
      if(that.data.banned.length==0){
      that.setData({
      status:false
      });
      }else{
      that.setData({
      status:true
      })
      }
      },
      fail: function(res) {
      console.log(res+'aaaaa')
      }
      });
    },

  setPreference:function()
  { wx.setStorage({ key:'preference',
    data:this.data.preference}),
    wx.setStorage({ key:'banned',
    data:this.data.banned})
    wx.navigateTo({
    url: '../setPreferencePage/setPreferencePage',})

  },

  removebanned:function(event)
  { var ban = this.data.banned;
    var id =event.currentTarget.dataset.id;
    var pre = this.data.preference;
    pre.push(ban[id]) 
    ban.splice(id,1);
    wx.setStorage({ key:'preference',
    data:pre}),
    wx.setStorage({ key:'banned',
    data:ban})
    wx.navigateTo({
    url: '../setPreferencePage/setPreferencePage',
  })},

  addbanned:function(event)
  { var ban = this.data.banned;
    var id =event.currentTarget.dataset.id;
    var pre = this.data.preference;
    ban.push(pre[id]) 
    pre.splice(id,1);
    wx.setStorage({ key:'preference',
    data:pre}),
    wx.setStorage({ key:'banned',
    data:ban})
    wx.navigateTo({
    url: '../setPreferencePage/setPreferencePage',
  })},


})
