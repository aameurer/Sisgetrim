import xml.etree.ElementTree as ET
import zipfile

def get_sheet_names(file_path):
    try:
        with zipfile.ZipFile(file_path, 'r') as zip_ref:
            # Read workbook.xml.rels to map rId to sheet name
            with zip_ref.open('xl/workbook.xml') as workbook_file:
                tree = ET.parse(workbook_file)
                root = tree.getroot()
                
                # Namespaces
                ns = {'main': 'http://schemas.openxmlformats.org/spreadsheetml/2006/main'}
                
                sheets = []
                for sheet in root.findall('main:sheets/main:sheet', ns):
                    sheets.append(sheet.get('name'))
                
                return sheets
    except Exception as e:
        return [f"Erro: {e}"]

file_path = r"c:\sisgetrim\CAD.FISCAL (ITBI) FERNANDO 2025-01-19 importar no sisgetrim.xlsx"
print(f"Abas: {get_sheet_names(file_path)}")
