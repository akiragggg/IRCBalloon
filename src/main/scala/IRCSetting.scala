package org.bone.ircballoon

import org.eclipse.swt.widgets.{List => SWTList, _}
import org.eclipse.swt.layout._
import org.eclipse.swt.events._
import org.eclipse.swt.graphics._
import org.eclipse.swt.custom.StyledText
import org.eclipse.swt.custom.StackLayout

import org.eclipse.swt._

class IRCSetting(parent: Composite, onModify: ModifyEvent => Any) extends 
      Composite(parent, SWT.NONE) with SWTHelper
{
    val gridLayout = new GridLayout(2,  false)
    val hostText = createText(this, "IRC 伺服器主機：")
    val portText = createText(this, "IRC 伺服器Port：")
    val password = createText(this, "IRC 伺服器密碼：", SWT.PASSWORD)
    val nickname = createText(this, "暱稱：")
    val channel = createText(this, "聊天頻道：")

    def getPassword = password.getText.trim match {
        case ""    => None
        case value => Some(value)
    }

    def createIRCBot(callback: String => Any, onLog: String => Any) = {

        if (!isSettingOK) {
            throw new Exception("IRC 設定不完整")
        }

        new IRCBot(
            hostText.getText, portText.getText.toInt, nickname.getText, 
            getPassword, channel.getText, callback, onLog
        )
    }

    def isSettingOK = {
        val hostname = this.hostText.getText.trim
        val port = this.portText.getText.trim
        val nickname = this.nickname.getText.trim
        val channel = this.channel.getText.trim

        hostname.length > 0 &&
        port.length > 0 &&
        nickname.length > 0 &&
        channel.length > 0 && channel.startsWith("#")
    }

    def setDefaultValue()
    {
        portText.setText("6667")
    }

    def setTextVerify()
    {
        portText.addVerifyListener { e: VerifyEvent => e.doit = e.text.forall(_.isDigit) }
    }

    def setModifyListener()
    {
        hostText.addModifyListener(onModify)
        portText.addModifyListener(onModify)
        password.addModifyListener(onModify)
        nickname.addModifyListener(onModify)
        channel.addModifyListener(onModify)
    }

    this.setDefaultValue()
    this.setTextVerify()
    this.setModifyListener()
    this.setLayout(gridLayout)
}

