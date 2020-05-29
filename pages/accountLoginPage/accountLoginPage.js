// pages/databaseGuide/databaseGuide.js

const app = getApp()

Page({
  data: { 
    adminName: '', 
    password:''
    },
    adminNameInput :function (e) { 
      this.setData({ 
        adminName:e.detail.value 
      }) 
      }, 
    passwordInput :function (e) { 
      this.setData({ 
        password:e.detail.value 
        }) 
      },
    onClickRegister:function()
    {wx.navigateTo({
      url: '../registrationPage/registrationPage',
    })


    },
    onClickSubmit:function(){
      if(this.data.adminName.length == 0 || this.data.password.length == 0){ 
        wx.showToast({ 
        title: '用户名密码不能为空', 
        icon: 'loading', 
        duration: 2000 
        }) 
       }
       else { 
        wx.switchTab({
          url: '../index/index',
        })
        //登录校验设置
        wx.showToast({ 
        title: '登录成功', 
        icon: 'success', 
        duration: 2000 
        }) 
        } 
        } 
       
  })