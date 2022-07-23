using Microsoft.Win32;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Security.Principal;

namespace EnvsrWinNative
{
    public class EnvsrWinSupport
    {
        private const int HWND_BROADCAST = 0xffff;
        private const uint WM_SETTINGCHANGE = 0x001a;

        [DllImport("user32.dll", SetLastError = true, CharSet = CharSet.Auto)]
        private static extern bool SendNotifyMessage(IntPtr hWnd, uint Msg, UIntPtr wParam, string lParam);

        public bool NotifyEnvironmentChange()
        {
            return SendNotifyMessage((IntPtr)HWND_BROADCAST, WM_SETTINGCHANGE, (UIntPtr)0, "Environment");
        }

        private void SystemEvents_UserPreferenceChanged(object sender, UserPreferenceChangedEventArgs e)
        {
            if (e.Category == UserPreferenceCategory.General)
            {
                // TODO 这里可能是环境变量更改
            }
        }


        public void RegisterUserPreferenceChangedCallback()
        {
            SystemEvents.UserPreferenceChanged += SystemEvents_UserPreferenceChanged;
        }

        private EnvironmentVariableTarget GetTargetByType(int type)
        {
            EnvironmentVariableTarget target;
            switch (type)
            {
                case 0:
                    target = EnvironmentVariableTarget.Process;
                    break;
                case 1:
                    target = EnvironmentVariableTarget.Machine;
                    break;
                case 2:
                    target = EnvironmentVariableTarget.User;
                    break;
                default:
                    throw new ArgumentOutOfRangeException("type");
            }
            return target;
        }

        /// <summary>
        /// 获取所有环境变量
        /// </summary>
        /// <param name="type">0: 所有环境变量(只读), 1: 系统环境变量, 2: 用户环境变量.</param>
        /// <returns></returns>
        /// <exception cref="ArgumentOutOfRangeException"></exception>

        public Dictionary<string, string> GetAllEnvironment(int type = 0)
        {
            var envs = new Dictionary<string, string>();

            var dictionary = Environment.GetEnvironmentVariables(GetTargetByType(type));
            foreach (DictionaryEntry entires in dictionary)
            {
                envs.Add(entires.Key as string, entires.Value as string);
            }
            return envs;
        }

        public void SetEnvironmentVariable(string key, string value, int type = 0)
        {
            Environment.SetEnvironmentVariable(key, value, GetTargetByType(type));
        }

        public string GetEnvironmentVariable(string key, int type = 0)
        {
            return Environment.GetEnvironmentVariable(key, GetTargetByType(type));
        }

        public string ExpandEnvironmentVariable(string value)
        {
            return Environment.ExpandEnvironmentVariables(value);
        }

        public bool IsAdministrator()
        {
            WindowsIdentity current = WindowsIdentity.GetCurrent();
            WindowsPrincipal windowsPrincipal = new WindowsPrincipal(current);
            return windowsPrincipal.IsInRole(WindowsBuiltInRole.Administrator);
        }
    }
}
