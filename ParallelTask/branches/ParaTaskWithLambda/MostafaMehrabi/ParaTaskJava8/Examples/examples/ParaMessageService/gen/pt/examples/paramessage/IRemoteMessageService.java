/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\PhD\\CourseWork\\ParalleIT\\ParallelTask\\branches\\ParaTaskWithLambda\\ParaTaskJava8\\doc\\examples\\ParaMessageService\\src\\pt\\examples\\paramessage\\IRemoteMessageService.aidl
 */
package pt.examples.paramessage;
/* The name of the remote service */
public interface IRemoteMessageService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements pt.examples.paramessage.IRemoteMessageService
{
private static final java.lang.String DESCRIPTOR = "pt.examples.paramessage.IRemoteMessageService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an pt.examples.paramessage.IRemoteMessageService interface,
 * generating a proxy if needed.
 */
public static pt.examples.paramessage.IRemoteMessageService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof pt.examples.paramessage.IRemoteMessageService))) {
return ((pt.examples.paramessage.IRemoteMessageService)iin);
}
return new pt.examples.paramessage.IRemoteMessageService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getMessage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getMessage();
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements pt.examples.paramessage.IRemoteMessageService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/* A simple Method which will return a message string */
@Override public java.lang.String getMessage() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMessage, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
/* A simple Method which will return a message string */
public java.lang.String getMessage() throws android.os.RemoteException;
}
