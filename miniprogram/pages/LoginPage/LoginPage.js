// pages/databaseGuide/databaseGuide.js

const app = getApp()

Page({
  data: {
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    wxbound:0,
  },
  jumpaccountLogin:function(){wx.navigateTo({url: '../accountLoginPage/accountLoginPage', })
  },
  checkUserInfo: function()
  {wx.login({
    success (res) {
      if (res.code) {
        console.log(res.code)
        //发起网络请求
        wx.request({
          url: 'https://api.ltzhou.com/user/login',
          data: {
            code: res.code
          },
          success(res){console.log("return");console.log(res.data)}
        })
      } else {
        console.log('登录失败！' + res.errMsg)
      }
    }
  })

    if(wx.canIUse('button.open-type.getUserInfo')){wx.switchTab({url: '../index/index', })}},
  onLoad: function() {
    // 查看是否授权
    wx.getSetting({
      success (res){
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称
          wx.getUserInfo({
            success: function(res) {
              console.log(res.userInfo)
            }
          })
        }
      }
    })
  },
  bindGetUserInfo (e) {
    console.log(e.detail.userInfo)
  }})