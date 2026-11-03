package com.andrerinas.openheadunit.connection.usb

import android.app.PendingIntent
import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.andrerinas.openheadunit.aap.AapService
import com.andrerinas.openheadunit.utils.ToastUtils
import org.junit.runner.RunWith
import org.junit.Test
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = android.app.Application::class)
class UsbPermissionRequestTest {
    @Test fun `overlapping scans request permission once per attached device`() {
        mockStatic(PendingIntent::class.java).use { pendingIntentCalls ->
        mockStatic(ToastUtils::class.java).use {
            val permissionIntent = mock(PendingIntent::class.java)
            pendingIntentCalls.`when`<PendingIntent> {
                PendingIntent.getBroadcast(any(), anyInt(), any(), anyInt())
            }.thenReturn(permissionIntent)
            val service = mock(AapService::class.java)
            val usb = mock(UsbManager::class.java)
            val device = mock(UsbDevice::class.java)
            `when`(service.getSystemService(Context.USB_SERVICE)).thenReturn(usb)
            `when`(service.getString(anyInt())).thenReturn("USB permission")
            `when`(device.deviceName).thenReturn("/dev/bus/usb/001/002")
            `when`(device.vendorId).thenReturn(0x18d1)
            `when`(device.productId).thenReturn(0x2d00)

            val manager = UsbLauncherManager(service)
            manager.requestPermission(device)
            manager.requestPermission(device)
            verify(usb, times(1)).requestPermission(eq(device), eq(permissionIntent))

            val reenumerated = mock(UsbDevice::class.java)
            `when`(reenumerated.deviceName).thenReturn("/dev/bus/usb/001/003")
            `when`(reenumerated.vendorId).thenReturn(0x18d1)
            `when`(reenumerated.productId).thenReturn(0x2d00)
            manager.requestPermission(reenumerated)
            verify(usb, times(1)).requestPermission(eq(reenumerated), eq(permissionIntent))

            manager.clearPermissionRequest(device)
            manager.requestPermission(device)
            verify(usb, times(2)).requestPermission(eq(device), eq(permissionIntent))
        }
        }
    }
}
