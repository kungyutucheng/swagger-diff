package com.kungyu.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.Messages;
import com.kungyu.model.common.UrlPair;
import com.kungyu.util.HttpUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wengyongcheng
 * @since 2020/6/28 10:22 上午
 */
public class GlobalSettings implements Configurable, Configurable.Composite {
    private JTextArea oldUrlTextArea;
    private JTextArea newUrlTextArea;
    private JLabel oldTextLabel;
    private JPanel mainPanel;
    private JLabel newTextLabel;
    private JButton beginDiffBtn;
    private JComboBox projectCombox;
    private JButton addProjectBtn;

    private MainSettings mainSettings = MainSettings.getInstance();

    @Override
    @Nls(capitalization = Nls.Capitalization.Title)
    public  String getDisplayName() {
        return "Swagger Diff";
    }

    @NotNull
    @Override
    public Configurable[] getConfigurables() {
        return new Configurable[0];
    }

    @Override
    @Nullable
    public JComponent createComponent() {
        // 初始化下拉框列表，新旧url配置数据也初始化为下拉列表的第一个元素的配置
        Map<String, UrlPair> configMap = mainSettings.getSettingMap();
        if (!MapUtils.isEmpty(configMap)) {
            for (Map.Entry<String, UrlPair> entry : configMap.entrySet()) {
                projectCombox.addItem(entry.getKey());
            }
            projectCombox.setSelectedIndex(0);
            Object selectedItem = projectCombox.getSelectedItem();
            if (selectedItem != null) {
                UrlPair urlPair = configMap.get(projectCombox.getSelectedItem().toString());
                if (urlPair != null) {
                    newUrlTextArea.setText(urlPair.getNewUrl());
                    oldUrlTextArea.setText(urlPair.getOldUrl());
                }
            }
        }

        // 下拉框添加选择监听事件，动态修改新旧url输入框的内容
        projectCombox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object selectedItem = e.getItem();
                if (selectedItem != null) {
                    UrlPair urlPair = configMap.get(selectedItem.toString());
                    if (urlPair != null) {
                        newUrlTextArea.setText(urlPair.getNewUrl());
                        oldUrlTextArea.setText(urlPair.getOldUrl());
                    } else {
                        newUrlTextArea.setText(null);
                        oldUrlTextArea.setText(null);
                    }
                }
            }
        });

        // 新增项目增加监听事件
        addProjectBtn.addActionListener(e -> {
            String newProjectName = Messages.showInputDialog("请输入项目名称", "新增配置", Messages.getInformationIcon());
            if (StringUtils.isNotBlank(newProjectName)) {
                if (configMap.containsKey(newProjectName)) {
                    Messages.showWarningDialog("已经存在该项目名称","新增配置失败");
                }
                // 更新数据
                configMap.put(newProjectName, null);
                mainSettings.setSettingMap(configMap);

                // 更新下拉列表
                projectCombox.addItem(newProjectName);
            }
        });

        beginDiffBtn.addActionListener(e -> {
            String selectedProjectName = (String) projectCombox.getSelectedItem();
            if (StringUtils.isBlank(selectedProjectName)) {
                Messages.showErrorDialog("请选中一个项目名称","对比错误");
                return;
            }
            String oldUrl = StringUtils.trim(oldUrlTextArea.getText());
            if (StringUtils.isBlank(oldUrl)) {
                Messages.showErrorDialog("前一个版本URL不能为空","对比错误");
                return;
            }
            String newUrl = StringUtils.trim(newUrlTextArea.getText());
            if (StringUtils.isBlank(newUrl)) {
                Messages.showErrorDialog("后一个版本URL不能为空", "对比错误");
                return;
            }
            String newUrlResponseStr = HttpUtil.doPost(newUrl, null);
            String oldUrlResponseStr = HttpUtil.doPost(oldUrl, null);

        });
        return mainPanel;
    }

    @Override
    public boolean isModified() {
        // 触发apply按钮的条件：当前项目配置发生变更
        Map<String, UrlPair> configMap = mainSettings.getSettingMap();
        if (MapUtils.isEmpty(configMap)) {
            return true;
        }
        String selectedProjectName = (String) projectCombox.getSelectedItem();
        if (StringUtils.isNotBlank(selectedProjectName)) {
            UrlPair urlPair = configMap.get(selectedProjectName);
            if (urlPair != null) {
                return !StringUtils.equals(urlPair.getNewUrl(), StringUtils.trim(newUrlTextArea.getText()))
                        || !StringUtils.equals(urlPair.getOldUrl(), StringUtils.trim(oldUrlTextArea.getText()));
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        Map<String, UrlPair> configMap = mainSettings.getSettingMap();
        String selectedProjectName = (String) projectCombox.getSelectedItem();
        if (StringUtils.isBlank(selectedProjectName)) {
            Messages.showErrorDialog("请至少配置一个项目","配置错误");
            return;
        }
        String oldUrl = StringUtils.trim(oldUrlTextArea.getText());
        if (StringUtils.isBlank(oldUrl)) {
            Messages.showErrorDialog("前一个版本URL不能为空","配置错误");
            return;
        }
        String newUrl = StringUtils.trim(newUrlTextArea.getText());
        if (StringUtils.isBlank(newUrl)) {
            Messages.showErrorDialog("后一个版本URL不能为空","配置错误");
            return;
        }
        if (MapUtils.isEmpty(configMap)) {
            configMap = new HashMap<>();
        }
        UrlPair urlPair = new UrlPair();
        urlPair.setNewUrl(newUrl);
        urlPair.setOldUrl(oldUrl);
        configMap.put(selectedProjectName, urlPair);
        mainSettings.setSettingMap(configMap);
    }
}
