// pages/databaseGuide/databaseGuide.js

const app = getApp()

Page({
  data: {
    userName: '', 
    password:'',
    phonenumber:'',
  },
  userNameInput :function (e) { 
    this.setData({ 
      userName:e.detail.value 
    }) 
    }, 
  passwordInput :function (e) { 
    this.setData({ 
      password:e.detail.value 
      }) 
    },
     
  register:function(){
    if(this.data.userName.length == 0 || this.data.password.length == 0 ){ 
      wx.showToast({ 
      title: '信息不能为空', 
      icon: 'loading', 
      duration: 2000 
      }) 
     }
     else { 
    wx.showToast({ 
      title: '注册成功', 
      icon: 'success', 
      duration: 2000 
      }) 
    wx.navigateTo({
    url: '../LoginPage/LoginPage',
  })}
},

  
 })