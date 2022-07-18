#include<iostream>
#include<string>

using namespace EnvsrWinNative;

bool NotifyEnviromentChange() {
	EnvsrWinNative::EnvsrWinSupport^ support = gcnew EnvsrWinSupport();
	return support->NotifyEnviromentChange();
}

System::Collections::Generic::Dictionary<System::String^, System::String^>^ GetAllEnviroment(int type) {
	EnvsrWinNative::EnvsrWinSupport^ support = gcnew EnvsrWinSupport();
	return support->GetAllEnviroment(type);
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

int main() {
	auto dict = GetAllEnviroment(0);
	for each (auto entry in dict)
	{
		std::cout << CSharpStringWrapper(entry.Key).cstr() << " -> " << CSharpStringWrapper(entry.Value).cstr() << std::endl;
	}
	return 0;
}