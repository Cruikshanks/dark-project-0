Const MSG_1 = 1, MSG_2 = 2, MSG_3 = 3

Type InGameMessageData
	Field x#
	Field y#
	Field numOfLines%
	Field imageSpacer%
	Field lineSpacer%
	Field charPrintDelay%
	Field quickPrint%
	Field lineWrap
	Field image
	
	Field message
	Field displayTime
	Field displayDelay
End Type

Function UpdateInGameMessages()

	numberOfMsgs = 0

	For m.InGameMessageData = Each InGameMessageData

		If m\displayTime + missionStartTime <= currentGameTime Then
			Dim inGameMessage$(m\numOfLines)	
			Select m\message
				
				Case MSG_1 : Restore msg1
				Case MSG_2 : Restore msg2
				Case MSG_3 : Restore msg3
		
			End Select
			
			For i = 1 To m\numOfLines
				Read msg_read$
				inGameMessage(i-1) = msg_read
			Next
			
		EndIf
		numberOfMsgs = numberOfMsgs + 1
	Next
	
	Return numberOfMsgs
	
End Function

Function CreateInGameMessage#(x,y,lns,imgSpacer,lnSpacer,dly,qkPt,lnWrap,img,msg,dTime,dDelay)

	m.InGameMessageData = New InGameMessageData
	
	m\x = x
	m\y = y
	m\numOfLines = lns
	m\imageSpacer = imgSpacer
	m\lineSpacer = lnSpacer
	m\charPrintDelay = dly
	m\quickPrint = qkPt
	m\lineWrap = lnWrap
	m\image = img
	
	m\message = msg
	m\displayTime = dTime
	m\displayDelay = dDelay
	
	time# = InGameMessageTimer(msg,lns,dly,dDelay)
	Return time#

End Function

Function CreateEndGameMessage(i.ImageData, winOrLose)

	image = CreateImage(WIDTH, HEIGHT)
	SetBuffer ImageBuffer(image)

	x = WIDTH / 2
	
	If winOrlose Then
		y = 45
		Color 160, 0, 0
		Restore wonGameMsg
	Else
		y = 100
		Color 255, 0, 0
		SetFont largeFont
		Restore lostGameMsg
	EndIf
		
	Repeat
		Read readMsg$
		If Not readMsg = "999"
			Read spacer
			Text x,y,readMsg,True,False
			y = y + FontHeight() + spacer
		EndIf
	Until readMsg = "999"
	
	i\width = ImageWidth(image)
	i\height = ImageHeight(image)
	i\image = image
	MaskImage i\image, 0, 0, 0
		
	SetBuffer BackBuffer()
	SetFont mainFont
	
End Function

Function InGameMessageTimer#(message,numOfLines,printDelay,displayDelay)

	time# = 0
	
	Select message				
		Case MSG_1 : Restore msg1
		Case MSG_2 : Restore msg2
		Case MSG_3 : Restore msg3
	End Select

	For i = 0 To (numOfLines - 1)
		Read msg_read$
		For j = 1 To Len(msg_read$)+1
			time# = time# + printDelay
		Next
	Next
	
	time# = (time# + displayDelay) / 1000

	Return time#

End Function

Function RenderInGameMessages()

	Color 0, 180, 0

	For m.InGameMessageData = Each InGameMessageData
		If m\displayTime + missionStartTime <= currentGameTime Then
			Flip
			SetBuffer FrontBuffer()
			DisplayInGameMessage(m)	
			FlushKeys
			SetBuffer BackBuffer()
			Delay(m\displayDelay * ONESECOND)
			Delete m
			Cls
		EndIf
	Next
	
End Function

Function DisplayInGameMessage(m.InGameMessageData)

	startX% = m\x
	startY% = m\y
	printDelay% = m\charPrintDelay
	lnWrap = m\lineWrap

	If m\image Then
		DrawImage m\image, m\x, m\y 
		startX = m\x + (ImageWidth(m\image) + m\imageSpacer)
	EndIf
	
	For i = 0 To (m\numOfLines - 1)
	
		x = startX
		
		For j = 1 To Len(inGameMessage$(i))+1

			If m\quickPrint Then
				If KeyHit(m\quickPrint) Then printDelay = 0
			EndIf
			
			Delay(printDelay%)
			Text x, startY, (Mid$(inGameMessage$(i), j, 1)), False, False
			x = x + StringWidth(Mid$(inGameMessage$(i), j, 1))
			
		Next
		
		startY = startY + StringHeight(inGameMessage$(i))
		
		If lnWrap Then
		
			If startY > (m\y + ImageHeight(m\image) + m\imageSpacer) Then 
				startX = m\x
				lnWrap = False
			EndIf
		EndIf
	Next
	
End Function