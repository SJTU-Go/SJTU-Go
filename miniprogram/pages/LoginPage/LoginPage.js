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
          url: 'https://api.ltzhou.com/user/login?code='+res.code,
          method:'POST',
          success(res){console.log("returningcode");console.log(res.data)
        wx.setStorage({
          data:res.data.userID,
          key: 'userID',
        })


        wx.request({
          url: 'https://api.ltzhou.com/user/preference/get?userID='+res.data.userID,
          method:'POST',
          success(res){if(res.data){
            var preelist=res.data.preferencelist.split(",")
            for(var i = 0;i<preelist.length;i++){
              if(preelist[i]==''||preelist[i]==null||typeof(preelist[i])==undefined){
                preelist.splice(i,1);
                  i=i-1;
              }
          }
            console.log(preelist)
            var bannlist=res.data.banlist.split(",")
            for(var i = 0;i<bannlist.length;i++){
              if(bannlist[i]==''||bannlist[i]==null||typeof(bannlist[i])==undefined){
                bannlist.splice(i,1);
                  i=i-1;
              }
          }


            console.log(bannlist)
            if(preelist){wx.setStorage({
            data: preelist,
            key: 'preference',
          })}
          if(bannlist){wx.setStorage({
            data:bannlist, 
            key: 'banned',
          })}
        }
        }})

        wx.request({
          url: 'https://api.ltzhou.com/user/history/get?userID='+res.data.userID,
          method:'POST',
          success(res){wx.setStorage({
            key: 'historygained',
            data: res.data,
          })
        }})


wx.switchTab({url: '../index/index', })
        }
        })
      } else {
        console.log('登录失败！' + res.errMsg)
        wx.showToast({ 
          title: '登录失败', 
          icon: 'loading', 
          duration: 2000 
          }) 
          
      }
    }
    
  }
  )



},
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