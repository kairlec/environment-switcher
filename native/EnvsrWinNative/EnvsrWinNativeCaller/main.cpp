#include<cstring>

#define DllExport __declspec(dllexport)

System::Collections::Generic::Dictionary<System::String^, System::String^>^ GetAllEnvironment(int type) {
	EnvsrWinNative::EnvsrWinSupport^ support = gcnew EnvsrWinNative::EnvsrWinSupport();
	return support->GetAllEnvironment(type);
}

class CSharpStringWrapper {
private:
	System::IntPtr _ptr;
	const char* _cstr = nullptr;
public:
	CSharpStringWrapper(System::String^ csstr) {
		_ptr = System::Runtime::InteropServices::Marshal::StringToCoTaskMemAnsi(csstr);
		_cstr = (const char*)(void*)_ptr;
	}
	const char* cstr() const {
		return _cstr;
	}
	~CSharpStringWrapper() {
		System::Runtime::InteropServices::Marshal::FreeCoTaskMem(_ptr);
	}
};

extern "C" {
	DllExport int NotifyEnvironmentChange() {
		EnvsrWinNative::EnvsrWinSupport^ support = gcnew EnvsrWinNative::EnvsrWinSupport();
		return support->NotifyEnvironmentChange();
	}
	
	DllExport unsigned long long GetAllEnvironment(int type, char buffer[]) {
		auto dict = GetAllEnvironment(type);
		unsigned long long offset = 0;
		for each (auto entry in dict)
		{
			auto key = CSharpStringWrapper(entry.Key).cstr();
			auto keyLen = strlen(key) + 1;
			memcpy(buffer + offset, key, keyLen);
			offset += keyLen;
			auto value = CSharpStringWrapper(entry.Value).cstr();
			auto valueLen = strlen(value) + 1;
			memcpy(buffer + offset, value, valueLen);
			offset += valueLen;
		}
		return offset;
	}

	DllExport void SetEnvironmentVariable(char* key, char* value, int type) {
		EnvsrWinNative::EnvsrWinSupport^ support = gcnew EnvsrWinNative::EnvsrWinSupport();
		auto cskey = System::Runtime::InteropServices::Marshal::PtrToStringAnsi((System::IntPtr)key);
		auto csvalue = System::Runtime::InteropServices::Marshal::PtrToStringAnsi((System::IntPtr)value);
		support->SetEnvironmentVariable(cskey, csvalue, type);
	}

	DllExport int IsAdministrator() {
		EnvsrWinNative::EnvsrWinSupport^ support = gcnew EnvsrWinNative::EnvsrWinSupport();
		return support->IsAdministrator();
	}
}
