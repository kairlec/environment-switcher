#include <cstring>
#include <msclr/gcroot.h>

#define DllExport __declspec(dllexport)

class EnvsrWinSupportHandle {
private:
	msclr::gcroot<EnvsrWinNative::EnvsrWinSupport^> support;
public:
	EnvsrWinSupportHandle() {
		support = gcnew EnvsrWinNative::EnvsrWinSupport();
	}
	System::Collections::Generic::Dictionary<System::String^, System::String^>^ getAllEnvironment(int type) const {
		return support->GetAllEnvironment(type);
	}
	bool notifyEnvironmentChange() const {
		return support->NotifyEnvironmentChange();
	}
	void setEnvironmentVariable(char* key, char* value, int type) const {
		auto cskey = System::Runtime::InteropServices::Marshal::PtrToStringAnsi((System::IntPtr)key);
		auto csvalue = System::Runtime::InteropServices::Marshal::PtrToStringAnsi((System::IntPtr)value);
		support->SetEnvironmentVariable(cskey, csvalue, type);
	}
	bool isAdministrator() const {
		return support->IsAdministrator();
	}
	System::String^ getEnvironmentVariable(char* key, int type) const {
		return support->GetEnvironmentVariable(System::Runtime::InteropServices::Marshal::PtrToStringAnsi((System::IntPtr)key), type);
	}
	System::String^ expandEnvironmentVariable(char* value) const {
		return support->ExpandEnvironmentVariable(System::Runtime::InteropServices::Marshal::PtrToStringAnsi((System::IntPtr)value));
	}
};

const static auto handler = EnvsrWinSupportHandle();

extern "C" {
	DllExport int notifyEnvironmentChange() {
		return handler.notifyEnvironmentChange();
	}

	DllExport unsigned long long expandEnvironmentVariable(char* value, char buffer[]) {
		auto csstr = handler.expandEnvironmentVariable(value);
		auto valuePtr = System::Runtime::InteropServices::Marshal::StringToCoTaskMemAnsi(csstr);
		auto cvalue = (char*)(void*)valuePtr;
		auto valueLen = strlen(cvalue);
		memcpy(buffer, cvalue, valueLen + 1);
		return valueLen;
	}

	DllExport unsigned long long getEnvironmentVariable(char* key, int type, char buffer[]) {
		auto csstr = handler.getEnvironmentVariable(key, type);
		if (csstr == nullptr) {
			return -1;
		}
		auto valuePtr = System::Runtime::InteropServices::Marshal::StringToCoTaskMemAnsi(csstr);
		auto cvalue = (char*)(void*)valuePtr;
		auto valueLen = strlen(cvalue);
		memcpy(buffer, cvalue, valueLen + 1);
		return valueLen;
	}

	DllExport unsigned long long getAllEnvironment(int type, char buffer[]) {
		auto dict = handler.getAllEnvironment(type);
		unsigned long long offset = 0;
		for each (auto entry in dict)
		{
			auto keyPtr = System::Runtime::InteropServices::Marshal::StringToCoTaskMemAnsi(entry.Key);
			auto key = (char*)(void*)keyPtr;
			auto keyLen = strlen(key) + 1;
			memcpy(buffer + offset, key, keyLen);
			offset += keyLen;
			auto valuePtr = System::Runtime::InteropServices::Marshal::StringToCoTaskMemAnsi(entry.Value);
			auto value = (char*)(void*)valuePtr;
			auto valueLen = strlen(value) + 1;
			memcpy(buffer + offset, value, valueLen);
			offset += valueLen;
			System::Runtime::InteropServices::Marshal::FreeCoTaskMem(keyPtr);
			System::Runtime::InteropServices::Marshal::FreeCoTaskMem(valuePtr);
		}
		return offset;
	}

	DllExport void setEnvironmentVariable(char* key, char* value, int type) {
		handler.setEnvironmentVariable(key, value, type);
	}

	DllExport int isAdministrator() {
		return handler.isAdministrator();
	}
}

